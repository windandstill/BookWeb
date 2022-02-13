package cn.aka.goods.user.category.servlet;

import cn.aka.goods.user.category.domain.Category;
import cn.aka.goods.user.category.service.imp.CategoryServiceImp;
import cn.aka.goods.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/categorySerlvet")
public class CategoryServlet extends BaseServlet {
    private CategoryServiceImp categoryService = new CategoryServiceImp();

    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> parents = categoryService.findAll();
        request.setAttribute("parents", parents);
        request.getRequestDispatcher("../jsps/left.jsp").forward(request,response);
    }
}
