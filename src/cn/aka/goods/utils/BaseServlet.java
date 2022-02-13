package cn.aka.goods.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        response.setContentType("text/html;charset=UTF-8");
        //获取表单路径后面带的参数method=方法名中的方法名
        String methodName = request.getParameter("method");

        Method method = null;

        try {
            //获得方法对象
            method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        } catch (Exception var10) {
            throw new RuntimeException("您要调用的方法：" + methodName + "它不存在！", var10);
        }
        String result = null;

        try {
            /**
             * 获取调用请求后处理的方法返回的字符串,来确定是转发还是重定向
             * "/index.jsp" 表示转发到index.jsp
             * "f:/index.jsp" f前缀表示forward 转发到index.jsp
             * "r:/index.jsp" r前缀表示redirect 重定向到index.jsp
             * null 或者 "" 表示既不转发又不重定向
             *  想重定向到百度,return null 自己去设置重定向
             */
            result = (String)method.invoke(this, request, response);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        if (result != null && !result.trim().isEmpty()) {
            int index = result.indexOf(":");
            if (index == -1) {
                request.getRequestDispatcher(result).forward(request, response);
                } else {
                    String start = result.substring(0, index);
                    String path = result.substring(index + 1);
                    if (start.equals("f")) {
                        request.getRequestDispatcher(path).forward(request, response);
                    } else if (start.equals("r")) {
                        response.sendRedirect(request.getContextPath() + path);

                    }
                }
            }
        }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}