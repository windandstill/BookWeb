package cn.aka.goods.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "AdminLoginFilter",urlPatterns = {"/adminjsps/admin/*"})
public class AdminLoginFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        Object admin = req.getSession().getAttribute("admin");
        if (admin == null) {
            request.setAttribute("msg", "您还没有登录，请登录!!！");
            request.getRequestDispatcher("/adminjsps/login.jsp").forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
