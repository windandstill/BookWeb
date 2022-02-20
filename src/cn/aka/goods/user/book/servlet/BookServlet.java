package cn.aka.goods.user.book.servlet;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.book.service.BookService;
import cn.aka.goods.user.book.service.imp.BookServiceImp;
import cn.aka.goods.user.pager.PageBean;
import cn.aka.goods.utils.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet("/user/bookServlet")
public class BookServlet extends BaseServlet {
    private BookService bookService = new BookServiceImp();
    /**
     * 获取当前页码
     */
    private int getPageNow(HttpServletRequest req) {
        int pageNow = 1;
        String param = req.getParameter("pageNow");
        if(param != null && !param.trim().isEmpty()) {
                pageNow = Integer.parseInt(param);
        }
        return pageNow;
    }

    /**
     * 截取url，页面中的分页导航中需要使用它做为超链接的目标！
     * http://localhost:8080/BookeWeb/user/bookServlet?methed=findByBname&bid=xxx&pageNow=3
     * /BookeWeb/user/bookServlet + methed=findByBname&bid=xxx&pageNow=3
     */
    private String getUrl(HttpServletRequest req) {
        String url = req.getRequestURI() + "?" + req.getQueryString();
         //如果url中存在pageNow参数，截取掉，如果不存在那就不用截取。
        int index = url.lastIndexOf("&pageNow=");
        if(index != -1) {
            url = url.substring(0, index);
        }
        return url;
    }

    /**
     * 按bid查询图书
     */
    public String load(HttpServletRequest req, HttpServletResponse resp) {
        String bid = req.getParameter("bid");
        Book book = bookService.findByBid(bid);
        //5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
        req.setAttribute("book", book);
        return "f:/jsps/book/desc.jsp";
    }
    /**
     * 按分类查
     */
    public String findByCategory(HttpServletRequest req, HttpServletResponse resp){
        //1. 得到pageNow：如果页面传递，使用页面的，如果没传，pageNow=1
        int pageNow = getPageNow(req);
        //2. 得到url：...
        String url = getUrl(req);
        //3. 获取查询条件，本方法就是cid，即分类的id
        String cid = req.getParameter("cid");
        //4. 使用pageNow和cid调用service#findByCategory得到PageBean
        PageBean<Book> pb = bookService.findByCategory(cid, pageNow);
        //5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
        pb.setUrl(url);
        req.setAttribute("pb", pb);
        return "f:/jsps/book/list.jsp";
    }

    /**
     *按作者查
     */
    public String findByAuthor(HttpServletRequest req, HttpServletResponse resp) {
        //1. 得到pageNow：如果页面传递，使用页面的，如果没传，pageNow=1
        int pageNow = getPageNow(req);
        //2. 得到url：...
        String url = getUrl(req);
        String author = req.getParameter("author");
        PageBean<Book> pb = bookService.findByAuthor(author, pageNow);
        //5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
        pb.setUrl(url);
        req.setAttribute("pb", pb);
        return "f:/jsps/book/list.jsp";
    }

    /**
     *按出版社查
     */
    public String findByPress(HttpServletRequest req, HttpServletResponse resp) {
        //1. 得到pageNow：如果页面传递，使用页面的，如果没传，pageNow=1
        int pageNow = getPageNow(req);
        //2. 得到url：...
        String url = getUrl(req);
        String press = req.getParameter("press");
        PageBean<Book> pb = bookService.findByPress(press, pageNow);
        //5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
        pb.setUrl(url);
        req.setAttribute("pb", pb);
        return "f:/jsps/book/list.jsp";
    }

    /**
     * 按书名查
     */
    public String findByBname(HttpServletRequest req, HttpServletResponse resp)  {
        //1. 得到pageNow：如果页面传递，使用页面的，如果没传，pageNow=1
        int pageNow = getPageNow(req);
        //2. 得到url：...
        String url = getUrl(req);
        String bname = req.getParameter("bname");
        PageBean<Book> pb = bookService.findByBname(bname, pageNow);
        //5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
        pb.setUrl(url);
        req.setAttribute("pb", pb);
        return "f:/jsps/book/list.jsp";
    }

    /**
     * 组合查询
     */
    public String findByCombination(HttpServletRequest req, HttpServletResponse resp) {
        //1. 得到pageNow：如果页面传递，使用页面的，如果没传，pageNow=1
        int pageNow = getPageNow(req);
        //2. 得到url：...
        String url = getUrl(req);
        Book book = new Book();
        book.setBname(req.getParameter("bname"));
        book.setAuthor(req.getParameter("author"));
        book.setPress(req.getParameter("press"));
        PageBean<Book> pb = bookService.findByCombination(book, pageNow);
        //5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
        pb.setUrl(url);
        req.setAttribute("pb", pb);
        return "f:/jsps/book/list.jsp";
    }
}
