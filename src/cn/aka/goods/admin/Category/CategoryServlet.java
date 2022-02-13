package cn.aka.goods.admin.Category;

import cn.aka.goods.user.book.service.imp.BookServiceImp;
import cn.aka.goods.user.category.domain.Category;
import cn.aka.goods.user.category.service.imp.CategoryServiceImp;
import cn.aka.goods.utils.BaseServlet;
import cn.itcast.commons.CommonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/categoryServlet")
public class CategoryServlet extends BaseServlet {
    private CategoryServiceImp categoryService = new CategoryServiceImp();
    private BookServiceImp bookService = new BookServiceImp();

    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> parents = categoryService.findAll();
        request.setAttribute("parents", parents);
        request.getRequestDispatcher("../adminjsps/admin/category/list.jsp").forward(request, response);
    }

    /**
     * 添加一级分类
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addParent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Category parent = CommonUtils.toBean(request.getParameterMap(), Category.class);
        parent.setCid(CommonUtils.uuid());
        categoryService.add(parent);
        findAll(request, response);
    }

    public void addChild(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Category child = CommonUtils.toBean(request.getParameterMap(), Category.class);
        child.setCid(CommonUtils.uuid());
        String pid = request.getParameter("pid");
        Category parent = new Category();
        parent.setCid(pid);
        child.setParent(parent);
        categoryService.add(child);
        findAll(request, response);
    }

    /**
     * 添加二级分类
     */
    public void addChildPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pid = request.getParameter("pid");
        List<Category> parents = categoryService.findParents();
        request.setAttribute("pid", pid);
        request.setAttribute("parents", parents);
        request.getRequestDispatcher("../adminjsps/admin/category/add2.jsp").forward(request, response);
    }

    public void editParentPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        Category parent = categoryService.load(cid);
        request.setAttribute("parent", parent);
        request.getRequestDispatcher("../adminjsps/admin/category/edit.jsp").forward(request, response);
    }

    public void editParent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Category parent = CommonUtils.toBean(request.getParameterMap(), Category.class);
        categoryService.edit(parent);
        findAll(request, response);
    }

    public void editChildPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        Category child = categoryService.load(cid);
        request.setAttribute("child", child);
        request.setAttribute("parents", categoryService.findParents());
        request.getRequestDispatcher("../adminjsps/admin/category/edit2.jsp").forward(request, response);

    }

    public void editChild(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Category child = CommonUtils.toBean(request.getParameterMap(), Category.class);
        String pid = request.getParameter("pid");
        Category parent = new Category();
        parent.setCid(pid);
        child.setParent(parent);
        categoryService.edit(child);
        findAll(request, response);
    }

    public void deleteParent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        int cnt = categoryService.findChildrenCountByParent(cid);
        if (cnt > 0) {
            request.setAttribute("msg", "该分类下还有二级标题，无法删除");
            request.getRequestDispatcher("../adminjsps/msg.jsp").forward(request, response);

        } else {
            categoryService.delete(cid);
            findAll(request, response);
        }
    }

    /**
     * 输出二级分类
     */
    public void deleteChild(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        int cnt = bookService.findBookCountByCategory(cid);
        if (cnt > 0) {
            request.setAttribute("msg", "该分类下还有图书，无法删除");
            request.getRequestDispatcher("../adminjsps/msg.jsp").forward(request, response);

        }else {
            categoryService.delete(cid);
            findAll(request,response);
        }
    }
}
