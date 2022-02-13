package cn.aka.goods.admin.admin;

import cn.aka.goods.utils.BaseServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/testServlet")
public class TestServlet extends BaseServlet {

    public String regist(HttpServletRequest request, HttpServletResponse response){
        try {
            response.sendRedirect("https://www.baidu.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
