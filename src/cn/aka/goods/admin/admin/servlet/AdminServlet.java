package cn.aka.goods.admin.admin.servlet;

import cn.aka.goods.admin.admin.domain.Admin;
import cn.aka.goods.admin.admin.service.AdminService;
import cn.aka.goods.admin.admin.service.imp.AdminServiceImp;
import cn.aka.goods.utils.BaseServlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/admin/adminServlet")
public class AdminServlet extends BaseServlet {
    private AdminService adminService = new AdminServiceImp();
    /**
     * 管理员登录功能
     */
    public String login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Admin form = new Admin();
        form.setAdminname(req.getParameter("adminname"));
        form.setAdminpwd(req.getParameter("adminpwd"));
        /*Admin form = CommonUtils.toBean(req.getParameterMap(),Admin.class);*/
        Admin admin = adminService.login(form);
        if(admin == null) {
            req.setAttribute("msg", "用户名或密码错误！");
            return "/adminjsps/login.jsp";
        }
        req.getSession().setAttribute("admin", admin);
        return "r:/adminjsps/admin/index.jsp";
    }

}
