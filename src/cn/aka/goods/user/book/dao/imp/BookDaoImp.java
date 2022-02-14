package cn.aka.goods.user.book.dao.imp;

import cn.aka.goods.user.book.dao.BookDao;
import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.category.domain.Category;
import cn.aka.goods.user.pager.PageBean;
import cn.aka.goods.user.pager.PageConstants;
import cn.aka.goods.utils.JDBCUtils;
import cn.itcast.commons.CommonUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BookDaoImp implements BookDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Book findByBid(String bid) {
        String sql = "SELECT * FROM goods.t_book b , goods.t_category c  WHERE b.cid=c.cid AND b.bid=?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<>(Book.class), bid);
    }

    /**
     * 按分类查询
     */
    @Override
    public PageBean<Book> findByCategory(String cid, int pageNow) {
        String sql="select * from t_book where cid= '"+cid+"'";
        return findByCriteria(sql,pageNow);
    }
    /**
     * 按书名模糊查询
     */
    @Override
    public PageBean<Book> findByBname(String bname, int pageNow) {
        String sql="select * from t_book where bname like '%" + bname + "%'";
        return findByCriteria(sql,pageNow);
    }
    /**
     * 按作者查
     */
    @Override
    public PageBean<Book> findByAuthor(String author, int pageNow) {
        String sql="select * from t_book where author like '%" + author + "%'";
        return findByCriteria(sql, pageNow);
    }
    /**
     * 按出版社查
     */
    @Override
    public PageBean<Book> findByPress(String press, int pageNow) {
        String sql="select * from t_book where press like '%" + press + "%'";
        return findByCriteria(sql, pageNow);
    }
    /**
     * 多条件组合查询
     */
    @Override
    public PageBean<Book> findByCombination(Book criteria, int pageNow) {
        String sql="select * from t_book where 1=1 ";
        if (!"".equals(criteria.getBname())){
            sql = sql+ "AND bname like '%" + criteria.getBname() +"%'";
        }
        if (!"".equals(criteria.getAuthor())){
            sql = sql+ "AND author like '%" + criteria.getAuthor() +"%'";
        }
        if (!"".equals(criteria.getPress())){
            sql = sql+ "AND press like '%" + criteria.getPress() +"%'";
        }
        return findByCriteria(sql, pageNow);
    }

    /**
     *写入sql 返回分页查询结果
     */
    @Override
    public PageBean<Book> findByCriteria(String sql, int pageNow) {
        int pageSize = PageConstants.BOOK_PAGE_SIZE;//每页记录数
        int totalRecords = 0;
        //查询一共有多少条数据
        List<Book> list = template.query(sql,new BeanPropertyRowMapper<>(Book.class));
        //得到了总记录数
       for(Book book :list){
           totalRecords++;
       }
        //得到beanList，即当前页记录
        sql = sql+" order by orderBy limit ?,?";
        List<Book> beanList = template.query(sql,new BeanPropertyRowMapper<>(Book.class),
        (pageNow-1) * pageSize,pageSize);
        PageBean<Book> pb = new PageBean<>();
        //其中PageBean没有url，这个任务由Servlet完成
        pb.setBeanList(beanList);
        pb.setPageNow(pageNow);
        pb.setPageSize(pageSize);
        pb.setTotalRecords(totalRecords);
        return pb;
    }

    @Override
    public int findBookCountByCategory(String cid) {
        String sql = "select count(*) from goods.t_book where cid = ?";
        Long total = template.queryForObject(sql, Long.class, cid);
        return total == null ? 0 : total.intValue();
    }

    @Override
    public void add(Book book) {
        String sql = "insert into t_book(bid,bname,author,price,currPrice," +
                "discount,press,publishtime,edition,pageNum,wordNum,printtime," +
                "booksize,paper,cid,image_w,image_b)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {book.getBid(),book.getBname(),book.getAuthor(),
                book.getPrice(),book.getCurrPrice(),book.getDiscount(),
                book.getPress(),book.getPublishtime(),book.getEdition(),
                book.getPageNum(),book.getWordNum(),book.getPrinttime(),
                book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
                book.getImage_w(),book.getImage_b()};
        template.update(sql,params);
    }
}
