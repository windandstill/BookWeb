package cn.aka.goods.user.book.service;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.pager.PageBean;

import java.sql.SQLException;

public interface BookService {
    /**
     * 加载图书
     */
    Book load(String bid);

    void edit(Book book);
    /**
     * 删除图书
     * @param bid
     * @throws SQLException
     */
    void delete(String bid);

    PageBean<Book> findByCategory(String cid, int pageNow) ;

    PageBean<Book> findByAuthor(String author, int pc) ;

    PageBean<Book> findByPress(String press, int pageNow) ;

    PageBean<Book> findByBname(String bname, int pageNow) ;

    PageBean<Book> findByCombination(Book book, int pageNow) ;

    Book findByBid(String bid);

    /**
     * 添加图书
     */
    public void add(Book book);

    /**
     * 返回二级标题下图书数量
     */
    public int findBookCountByCategory(String cid);
}
