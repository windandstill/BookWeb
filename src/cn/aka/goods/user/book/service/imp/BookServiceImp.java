package cn.aka.goods.user.book.service.imp;

import cn.aka.goods.user.book.dao.BookDao;
import cn.aka.goods.user.book.dao.imp.BookDaoImp;
import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.book.service.BookService;
import cn.aka.goods.user.pager.PageBean;

import java.sql.SQLException;

public class BookServiceImp implements BookService {
    private BookDao bookDao = new BookDaoImp();
    /**
     * 删除图书
     * @param bid
     * @throws SQLException
     */
    @Override
    public void delete(String bid){
    bookDao.delete(bid);
    }

    /**
     * 加载图书
     */
    @Override
    public Book load(String bid) {
        return bookDao.findByBid(bid);
    }

    @Override
    public void edit(Book book) {
        bookDao.edit(book);
    }

    @Override
    public PageBean<Book> findByCategory(String cid, int pageNow) {
        return bookDao.findByCategory(cid,pageNow);
    }

    @Override
    public PageBean<Book> findByAuthor(String author, int pageNow)  {
        return bookDao.findByAuthor(author,pageNow);
    }

    @Override
    public PageBean<Book> findByPress(String press, int pageNow)  {
        return bookDao.findByPress(press,pageNow);
    }

    @Override
    public PageBean<Book> findByBname(String bname, int pageNow)  {
        return bookDao.findByBname(bname,pageNow);
    }

    @Override
    public PageBean<Book> findByCombination(Book book, int pageNow) {
        return bookDao.findByCombination(book,pageNow);
    }

    @Override
    public Book findByBid(String bid) {
        return bookDao.findByBid(bid);
    }

    @Override
    public void add(Book book) {
        bookDao.add(book);
    }

    /**
     * 返回二级标题下图书数量
     * @param cid
     * @return
     */
    @Override
    public int findBookCountByCategory(String cid){
        return bookDao.findBookCountByCategory(cid);
    }
}
