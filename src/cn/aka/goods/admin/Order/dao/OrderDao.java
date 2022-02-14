package cn.aka.goods.admin.Order.dao;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.order.domain.Order;
import cn.aka.goods.user.order.domain.OrderItem;
import cn.aka.goods.user.pager.Expression;
import cn.aka.goods.user.pager.PageBean;
import cn.aka.goods.user.pager.PageConstants;
import cn.aka.goods.utils.JDBCUtils;
import cn.itcast.commons.CommonUtils;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询订单状态
     * @param oid
     * @return
     * @throws SQLException
     */
    public int findStatus(String oid) throws SQLException {
        String sql = "select status from t_order where oid=?";
        Integer status = template.queryForObject(sql,Integer.class,oid);
        return status;

    }

    /**
     * 修改订单状态
     * @param oid
     * @param status
     * @throws SQLException
     */
    public void updateStatus(String oid, int status) throws SQLException {
        String sql = "update t_order set status=? where oid=?";
        template.update(sql, status, oid);
    }

    /**
     * 加载订单
     * @param oid
     * @return
     * @throws SQLException
     */
    public Order load(String oid) throws SQLException {
        String sql = "select * from t_order where oid=?";

        List <Order> list=template.query(sql,new BeanPropertyRowMapper<Order>(Order.class),oid);
        for (Order order:list
             ) {
            loadOrderItem(order);//为当前订单加载它的所有订单条目
            return order;
        }

        return null;
    }

    /**
     * 生成订单
     * @param order
     * @throws SQLException
     */
    public void add(Order order) throws SQLException {
        /*
         * 1. 插入订单
         */
        String sql = "insert into t_order values(?,?,?,?,?,?)";
        Object[] params = {order.getOid(), order.getOrdertime(),
                order.getTotal(),order.getStatus(),order.getAddress(),
                order.getOwner().getUid()};
        template.update(sql, params);

        /*
         * 2. 循环遍历订单的所有条目,让每个条目生成一个Object[]
         * 多个条目就对应Object[][]
         * 执行批处理，完成插入订单条目
         */
        sql = "insert into t_orderitem values(?,?,?,?,?,?,?,?)";
        int len = order.getOrderItemList().size();
        Object[][] objs = new Object[len][];
        for(int i = 0; i < len; i++){
            OrderItem item = order.getOrderItemList().get(i);
            objs[i] = new Object[]{item.getOrderItemId(),item.getQuantity(),
                    item.getSubtotal(),item.getBook().getBid(),
                    item.getBook().getBname(),item.getBook().getCurrPrice(),
                    item.getBook().getImage_b(),order.getOid()};
        }
        template.batchUpdate(sql, String.valueOf(objs));
    }

    /**
     * 按用户查询订单
     * @param uid
     * @param pageNow
     * @return
     * @throws SQLException
     */
    public PageBean<Order> findByUser(String uid, int pageNow) throws SQLException {
        List<Expression> exprList = new ArrayList<Expression>();
        exprList.add(new Expression("uid", "=", uid));
        return findByCriteria(exprList, pageNow);
    }

    /**
     * 查询所有
     */
    public PageBean<Order> findAll(int pageNow) throws SQLException {
        List<Expression> exprList = new ArrayList<Expression>();
        return findByCriteria(exprList, pageNow);
    }

    /**
     * 按状态查询
     * @param status
     * @param pageNow
     * @return
     * @throws SQLException
     */
    public PageBean<Order> findByStatus(int status, int pageNow) throws SQLException {
        List<Expression> exprList = new ArrayList<Expression>();
        exprList.add(new Expression("status", "=", status + ""));
        return findByCriteria(exprList, pageNow);
    }

    private PageBean<Order> findByCriteria(List<Expression> exprList, int pageNow) throws SQLException {

        /*
         * 1. 得到pageSize
         * 2. 得到totalRecords
         * 3. 得到beanList
         * 4. 创建PageBean，返回
         */
        /*
         * 1. 得到pageSize
         */
        int pageSize = PageConstants.ORDER_PAGE_SIZE;//每页记录数

        /*
         * 2. 通过exprList来生成where子句
         */
        StringBuilder whereSql = new StringBuilder(" where 1=1");
        List<Object> params = new ArrayList<Object>();//SQL中有问号，它是对应问号的值
        for(Expression expr : exprList) {
            /*
             * 添加一个条件上，
             * 1) 以and开头
             * 2) 条件的名称
             * 3) 条件的运算符，可以是=、!=、>、< ... is null，is null没有值
             * 4) 如果条件不是is null，再追加问号，然后再向params中添加一与问号对应的值
             */
            whereSql.append(" and ").append(expr.getName())
                    .append(" ").append(expr.getOperator()).append(" ");
            // where 1=1 and bid = ?
            if(!expr.getOperator().equals("is null")) {
                whereSql.append("?");
                params.add(expr.getValue());
            }
        }

        /*
         * 3. 总记录数
         */
//        int totalRecords = 0;
        String sql = "select count(*) from goods.t_order"+whereSql;
//        List<Order> list =template.query(sql,new BeanPropertyRowMapper<Order>(Order.class),params.toArray());
          Integer totalRecords = template.queryForObject(sql,Integer.class,params.toArray());


////       得到了总记录数
//        for(Order order :list){
//           totalRecords++;
//        }
        /*
         * 4. 得到beanList，即当前页记录
         */
        sql = "select * from goods.t_order" + whereSql + " order by ordertime desc limit ?,?";

        params.add((pageNow-1) * pageSize);//当前页首行记录的下标
        params.add(pageSize);//一共查询几行，就是每页记录数

        List<Order> beanList = template.query(sql, new BeanPropertyRowMapper<Order>(Order.class),params.toArray());
        // 虽然已经获取所有的订单，但每个订单中并没有订单条目。
        // 遍历每个订单，为其加载它的所有订单条目
        for(Order order : beanList) {
            loadOrderItem(order);
        }

        /*
         * 5. 创建PageBean，设置参数
         */
        PageBean<Order> pb = new PageBean<Order>();
        /*
         * 其中PageBean没有url，这个任务由Servlet完成
         */
        pb.setBeanList(beanList);
        pb.setPageNow(pageNow);
        pb.setPageSize(pageSize);
        pb.setTotalRecords(totalRecords);

        return pb;
    }

    /*
     * 为指定的order载它的所有OrderItem
     */
    private void loadOrderItem(Order order) throws SQLException {
        /*
         * 1. 给sql语句select * from t_orderitem where oid=?
         * 2. 执行之，得到List<OrderItem>
         * 3. 设置给Order对象
         */
        String sql = "select * from t_orderitem where oid=?";
        List<Map<String,Object>> mapList = template.queryForList(sql,order.getOid());
        List<OrderItem> orderItemList = toOrderItemList(mapList);

        order.setOrderItemList(orderItemList);
    }

    /**
     * 把多个Map转换成多个OrderItem
     * @param mapList
     * @return
     */
    private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        for(Map<String,Object> map : mapList) {
            OrderItem orderItem = toOrderItem(map);
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    /*
     * 把一个Map转换成一个OrderItem
     */
    private OrderItem toOrderItem(Map<String, Object> map) {
        OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
        Book book = CommonUtils.toBean(map, Book.class);
        orderItem.setBook(book);
        return orderItem;
    }
}
