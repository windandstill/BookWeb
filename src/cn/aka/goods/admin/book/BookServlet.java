package cn.aka.goods.admin.book;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.book.service.BookService;
import cn.aka.goods.user.book.service.imp.BookServiceImp;
import cn.aka.goods.user.category.domain.Category;
import cn.aka.goods.user.category.service.imp.CategoryServiceImp;
import cn.aka.goods.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
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
}
