package cn.aka.goods.admin.book;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.book.service.BookService;
import cn.aka.goods.user.book.service.imp.BookServiceImp;
import cn.aka.goods.user.category.domain.Category;
import cn.aka.goods.user.category.service.imp.CategoryServiceImp;
import cn.aka.goods.user.pager.PageBean;
import cn.aka.goods.utils.BaseServlet;
import cn.itcast.commons.CommonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


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
     * 编辑图书
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        /*
         * 1. 把表单数据封装到Book对象中
         * 2. 封装cid到Category中
         * 3. 把Category赋给Book
         * 4. 调用service完成工作
         * 5. 保存成功信息，转发到msg.jsp
         */
        Map map = request.getParameterMap();
        Book book = CommonUtils.toBean(map, Book.class);
        Category category = CommonUtils.toBean(map, Category.class);
        book.setCategory(category);
        bookService.edit(book);
        request.setAttribute("msg", "修改图书成功！");
        return "f:/adminjsps/msg.jsp";
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
        //获取真实的路径
        String savepath = req.getServletContext().getRealPath("/");
        //删除文件
        new File(savepath, book.getImage_w()).delete();
        //删除文件
        new File(savepath, book.getImage_b()).delete();
        //删除数据库的记录
        bookService.delete(bid);
        req.setAttribute("msg", "删除图书成功！");
        return "f:/adminjsps/msg.jsp";
    }

    /**
     * 添加图书第一步,获取所有一级分类，保存之转发到add.jsp，该页面会在下拉列表中显示所有一级分类
     */
    public String addPre(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //1. 获取所有一级分类，保存之
        List<Category> parents = categoryService.findParents();
        req.setAttribute("parents", parents);
        //2. 转发到add.jsp，该页面会在下拉列表中显示所有一级分类
        return "f:/adminjsps/admin/book/add.jsp";
    }

    /**
     *ajax点击一级分类异步加载二级分类
     */
    public String ajaxFindChildren(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1. 获取pid
        String pid = req.getParameter("pid");
        // 2. 通过pid查询出所有2级分类
        List<Category> children = categoryService.findChildren(pid);
        // 3. 把List<Category>转换成json，输出给客户端
        String json = toJson(children);
        resp.getWriter().print(json);
        return null;
    }
    // {"cid":"fdsafdsa", "cname":"fdsafdas"}
    // {"cid":"fdsafdsa", "cname":"fdsafdas"}
    private String toJson(Category category) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
        sb.append(",");
        sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    // [{"cid":"fdsafdsa", "cname":"fdsafdas"}, {"cid":"fdsafdsa", "cname":"fdsafdas"}]
    private String toJson(List<Category> categoryList) {
        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i < categoryList.size(); i++) {
            sb.append(toJson(categoryList.get(i)));
            if(i < categoryList.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
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
     * 按bid查询图书 加载图书
     */
    public String load(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //1. 获取bid，得到Book对象，保存之
        String bid = req.getParameter("bid");
        Book book = bookService.load(bid);
        req.setAttribute("book", book);
        // 2. 获取所有一级分类，保存之
        req.setAttribute("parents", categoryService.findParents());
        //3. 获取当前图书所属的一级分类下所有2级分类
        String pid = book.getCategory().getParent().getCid();
        req.setAttribute("children", categoryService.findChildren(pid));
        //4. 转发到desc.jsp显示
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
