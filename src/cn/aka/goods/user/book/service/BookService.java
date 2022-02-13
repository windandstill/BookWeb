package cn.aka.goods.user.book.service;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.pager.PageBean;

import java.sql.SQLException;

public interface BookService {
    /**
     * 加载图书
     */
    Book load(String bid);

    void delete(String bid);


    PageBean<Book> findByCategory(String cid, int pageNow) throws SQLException;

    PageBean<Book> findByAuthor(String author, int pc) throws SQLException;

    PageBean<Book> findByPress(String press, int pageNow) throws SQLException;

    PageBean<Book> findByBname(String bname, int pageNow) throws SQLException;

    PageBean<Book> findByCombination(Book book, int pageNow) throws SQLException;

    Book findByBid(String bid);

    /**
     * 返回二级标题下图书数量
     * @param cid
     * @return
     */
    public int findBookCountByCategory(String cid);
}
