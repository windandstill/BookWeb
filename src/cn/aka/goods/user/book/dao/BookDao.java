package cn.aka.goods.user.book.dao;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.pager.PageBean;

import java.sql.SQLException;

public interface BookDao {
    Book findByBid(String bid);

    PageBean<Book> findByCategory(String cid, int pageNow) throws SQLException;

    PageBean<Book> findByBname(String bname, int pageNow) throws SQLException;

    PageBean<Book> findByAuthor(String author, int pageNow) throws SQLException;

    PageBean<Book> findByPress(String press, int pageNow) throws SQLException;

    PageBean<Book> findByCombination(Book criteria, int pageNow) throws SQLException;

    PageBean<Book> findByCriteria(String sql, int pageNow) throws SQLException;

    /**
     * 查询指定分类下图书个数
     * @param cid
     * @return
     */
    public int findBookCountByCategory(String cid);
}
