package cn.aka.goods.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class SetEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    /**
     *
     * 设置编码方式为utf-8
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request1=(HttpServletRequest) req;
        request1.setCharacterEncoding("utf-8");
        chain.doFilter(req, resp);
    }
}
