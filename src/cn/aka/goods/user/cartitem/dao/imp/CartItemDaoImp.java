package cn.aka.goods.user.cartitem.dao.imp;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.cartitem.dao.CartItemDao;
import cn.aka.goods.user.cartitem.domain.CartItem;
import cn.aka.goods.user.user.domain.User;
import cn.aka.goods.utils.JDBCUtils;
import cn.itcast.commons.CommonUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartItemDaoImp implements CartItemDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 将查询多表查询出来的map封装成多个对象
     * @param map
     * @return
     */
    private CartItem toCartIem(Map<String,Object> map){
        if(map == null) {
            return null;
        }
        CartItem cartItem = CommonUtils.toBean(map,CartItem.class);
        Book book = CommonUtils.toBean(map, Book.class);
        User user = CommonUtils.toBean(map,User.class);
        cartItem.setBook(book);
        cartItem.setUser(user);
        return cartItem;
    }

    /**
     * 将多个装有多个map的list映射成多个对象
     * @param mapList
     * @return
     */
    private List<CartItem> toCartItemList(List<Map<String, Object>> mapList){
        List<CartItem> cartItemList = new ArrayList<>();
        for (Map<String,Object> map : mapList){
            CartItem cartItem = toCartIem(map);
            cartItemList.add(cartItem);
        }
        return cartItemList;
    }

    /**
     * 通过购物车查询购物车列表
     */
    @Override
    public List<CartItem> findByUser(String uid) {
        String sql = "SELECT * FROM t_cartitem c, t_book b Where c.bid=b.bid and uid=? order by c.orderBy";
        List<Map<String, Object>> mapList = template.queryForList(sql, uid);
        return toCartItemList(mapList);
    }

    /**
     * 查询某个用户的某本图书的购物车条目是否存在
     */
    @Override
    public CartItem findByUidAndBid(String uid, String bid) {
        String sql ="select * from t_cartitem where uid=? and bid =?";
        List<CartItem> query = template.query(sql, new BeanPropertyRowMapper<>(CartItem.class), uid, bid);
        for (CartItem cartItem : query){
            return cartItem;
        }
        return null;
    }

    /**
     * 修改购物车指定条目的数量
     */
    @Override
    public void updateQuantity(String cartItemId, int quantity) {
        String sql ="update t_cartitem set quantity=? where cartItemId =?";
        template.update(sql,quantity,cartItemId);
    }

    /**
     * 添加购物车条目
     */
    @Override
    public void addCartItem(CartItem cartItem) {
        String sql = "insert into t_cartitem(cartItemId, quantity, bid, uid) VALUES (?,?,?,?)";
        template.update(sql,cartItem.getCartItemId(),cartItem.getQuantity(),cartItem.getBook().getBid(),cartItem.getUser().getUid());
    }

    /**
     * 通过购物id删除购物车内容
     * @param cartItemIds
     */
    @Override
    public void batchDelete(String cartItemIds){
        //把cartItemIds转换成数组
        String[] cartItemIdArray = cartItemIds.split(",");
        //把cartItemIds转换成一个where子句
        StringBuilder sb = new StringBuilder("cartItemId in(");
        for (int i = 0; i < cartItemIdArray.length; i++) {
            sb.append("?");
            if (i < cartItemIdArray.length-1){
                sb.append(",");
            }
        }
        sb.append(")");
        String sql = "delete from t_cartitem where "+sb;
        template.update(sql,cartItemIdArray);

    }
   /* private String toWhereSql(int len){
        StringBuilder sb = new StringBuilder("cartItemId in(");
        for (int i = 0; i < len; i++) {
            sb.append("?");
            if (i < len-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }*/

    /**
     * 按id查询
     */
    @Override
    public CartItem findByCartItemId(String cartItemId) {
        String sql = "select * from t_cartItem c, t_book b where c.bid=b.bid and c.cartItemId=?";
        Map<String,Object> map = template.queryForMap(sql,cartItemId);
        return toCartIem(map);
    }
}
