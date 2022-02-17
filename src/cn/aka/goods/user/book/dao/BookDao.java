package cn.aka.goods.user.book.dao;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.pager.PageBean;

import java.sql.SQLException;

public interface BookDao {
    /**
     * 删除图书
     * @param bid
     * @throws SQLException
     */
    void delete(String bid);

    /**
     * 修改图书
     */
    void edit(Book book);

    /**
     * 通过b id  连接查询 获取book和category中属性
     * @param bid
     * @return
     */
    Book findByBid(String bid);

    /**
     * 按分类查询
     */
    PageBean<Book> findByCategory(String cid, int pageNow) ;

    /**
     * 按书名模糊查询
     */
    PageBean<Book> findByBname(String bname, int pageNow) ;

    /**
     * 按作者查
     */
    PageBean<Book> findByAuthor(String author, int pageNow) ;

    /**
     * 按出版社查
     */
    PageBean<Book> findByPress(String press, int pageNow) ;

    /**
     * 多条件组合查询
     */
    PageBean<Book> findByCombination(Book criteria, int pageNow) ;

    /**
     * 查询指定分类下图书个数
     * @param cid
     * @return
     */
    int findBookCountByCategory(String cid);

    /**
     * 添加图书
     */
    void add(Book book);
}
