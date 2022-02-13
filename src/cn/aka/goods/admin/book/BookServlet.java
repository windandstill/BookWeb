package cn.aka.goods.admin.book;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.book.service.BookService;
import cn.aka.goods.user.book.service.imp.BookServiceImp;
import cn.aka.goods.user.category.domain.Category;
import cn.aka.goods.user.category.service.imp.CategoryServiceImp;
import cn.aka.goods.user.pager.PageBean;
import cn.aka.goods.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/bookServlet")
public class BookServlet extends BaseServlet {
    private BookService bookService = new BookServiceImp();
    private CategoryServiceImp categoryService = new CategoryServiceImp();

    /**
     *查询所有分类
     */
    public void findCategoryAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> parents = categoryService.findAll();
        request.setAttribute("parents", parents);
        request.getRequestDispatcher("../adminjsps/admin/book/left.jsp").forward(request,response);
    }

    /**
     * 删除图书
     */
    public String delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bid = req.getParameter("bid");
        /**
         * 删除图片
         */
        Book book = bookService.load(bid);
        String savepath = req.getServletContext().getRealPath("/");//获取真实的路径
        new File(savepath, book.getImage_w()).delete();//删除文件
        new File(savepath, book.getImage_b()).delete();//删除文件
        bookService.delete(bid);//删除数据库的记录
        req.setAttribute("msg", "删除图书成功！");
        return "f:/adminjsps/msg.jsp";
    }
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
     * http://localhost:8080/goods/BookServlet?methed=findByCategory&cid=xxx&pc=3
     * /goods/BookServlet + methed=findByCategory&cid=xxx&pc=3
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
    public String load(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String bid = req.getParameter("bid");
        Book book = bookService.findByBid(bid);
        //5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
        req.setAttribute("book", book);
        return "f:/adminjsps/admin/book/desc.jsp";
    }
    /**
     * 按分类查
     */
    public String findByCategory(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
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
        return "f:/adminjsps/admin/book/list.jsp";
    }

    /**
     *按作者查
     */
    public String findByAuthor(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        //1. 得到pageNow：如果页面传递，使用页面的，如果没传，pageNow=1
        int pageNow = getPageNow(req);
        //2. 得到url：...
        String url = getUrl(req);
        String author = req.getParameter("author");
        PageBean<Book> pb = bookService.findByAuthor(author, pageNow);
        //5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
        pb.setUrl(url);
        req.setAttribute("pb", pb);
        return "f:/adminjsps/admin/book/list.jsp";
    }
    /**
     *按出版社查
     */
    public String findByPress(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        //1. 得到pageNow：如果页面传递，使用页面的，如果没传，pageNow=1
        int pageNow = getPageNow(req);
        //2. 得到url：...
        String url = getUrl(req);
        String press = req.getParameter("press");
        PageBean<Book> pb = bookService.findByPress(press, pageNow);
        //5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
        pb.setUrl(url);
        req.setAttribute("pb", pb);
        return "f:/adminjsps/admin/book/list.jsp";
    }
    /**
     * 按书名查
     */
    public String findByBname(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        //1. 得到pageNow：如果页面传递，使用页面的，如果没传，pageNow=1
        int pageNow = getPageNow(req);
        //2. 得到url：...
        String url = getUrl(req);
        String bname = req.getParameter("bname");
        PageBean<Book> pb = bookService.findByBname(bname, pageNow);
        //5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
        pb.setUrl(url);
        req.setAttribute("pb", pb);
        return "f:/adminjsps/admin/book/list.jsp";
    }
    /**
     * 组合查询
     */
    public String findByCombination(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
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
        return "f:/adminjsps/admin/book/list.jsp";
    }
}
