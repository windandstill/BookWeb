package cn.aka.goods.admin.Order.servlet;

import cn.aka.goods.admin.Order.service.OrderService;
import cn.aka.goods.user.order.domain.Order;
import cn.aka.goods.user.pager.PageBean;
import cn.aka.goods.utils.BaseServlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/Admin/AdminOrderServlet")
public class AdminOrderServlet extends BaseServlet {
    private OrderService orderService = new OrderService();

    /**
     * 获取当前页码
     * @param req
     * @return
     */
    private int getPageNow(HttpServletRequest req) {
        int pageNow = 1;
        String param = req.getParameter("pageNow");
        if(param != null && !param.trim().isEmpty()) {
            pageNow = Integer.parseInt(param);
        }
        return pageNow;
    }
//    private int getPageNow(HttpServletRequest req) {
//        int pageNow = 1;
//        String param = req.getParameter("pageNow");
//        if(param != null && !param.trim().isEmpty()) {
//            try {
//                pageNow = Integer.parseInt(param);
//            } catch(RuntimeException e) {}
//        }
//        return pageNow;
//    }

    /**
     * 截取url，页面中的分页导航中需要使用它做为超链接的目标！
     * @param req
     * @return
     */
    /*
     * http://localhost:8080/goods/BookServlet?methed=findByCategory&cid=xxx&pageNow=3
     * /goods/BookServlet + methed=findByCategory&cid=xxx&pageNow=3
     */
//    private String getUrl(HttpServletRequest req) {
//        String url = req.getRequestURI() + "?" + req.getQueryString();
//        /*
//         * 如果url中存在pageNow参数，截取掉，如果不存在那就不用截取。
//         */
//        int index = url.lastIndexOf("&pageNow=");
//        if(index != -1) {
//            url = url.substring(0, index);
//        }
//        return url;
//    }
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
     * 查看所有订单
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String findAll(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException,SQLException {
        /*
         * 1. 得到pageNow：如果页面传递，使用页面的，如果没传，pageNow=1
         */
        int pageNow = getPageNow(req);
        /*
         * 2. 得到url：...
         */
        String url = getUrl(req);

        /*
         * 4. 使用pageNow和cid调用service#findByCategory得到PageBean
         */
        PageBean<Order> pb = orderService.findAll(pageNow);
        /*
         * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
         */
        pb.setUrl(url);
        req.setAttribute("pb", pb);
        return "f:/adminjsps/admin/order/list.jsp";
    }

    /**
     * 按状态查询
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String findByStatus(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        /*
         * 1. 得到pageNow：如果页面传递，使用页面的，如果没传，pageNow=1
         */
        int pageNow = getPageNow(req);
        /*
         * 2. 得到url：...
         */
        String url = getUrl(req);
        /*
         * 3. 获取链接参数：status
         */
        int status = Integer.parseInt(req.getParameter("status"));
        /*
         * 4. 使用pageNow和cid调用service#findByCategory得到PageBean
         */
        PageBean<Order> pb = orderService.findByStatus(status, pageNow);
        /*
         * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
         */
        pb.setUrl(url);
        req.setAttribute("pb", pb);
        return "f:/adminjsps/admin/order/list.jsp";
    }

    /**
     * 查看订单详细信息
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String load(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        String oid = req.getParameter("oid");
        Order order = orderService.load(oid);
        req.setAttribute("order", order);
        String btn = req.getParameter("btn");//btn说明了用户点击哪个超链接来访问本方法的
        req.setAttribute("btn", btn);
        return "/adminjsps/admin/order/desc.jsp";
    }

    /**
     * 取消订单
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String cancel(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String oid = req.getParameter("oid");
        /*
         * 校验订单状态
         */
        int status = orderService.findStatus(oid);
        if(status != 1) {
            req.setAttribute("code", "error");
            req.setAttribute("msg", "状态不对，不能取消！");
            return "f:/adminjsps/msg.jsp";
        }
        orderService.updateStatus(oid, 5);//设置状态为取消！
        req.setAttribute("code", "success");
        req.setAttribute("msg", "您的订单已取消");
        return "f:/adminjsps/msg.jsp";
    }

    /**
     * 发货功能
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String deliver(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String oid = req.getParameter("oid");
        /*
         * 校验订单状态
         */
        int status = orderService.findStatus(oid);
        if(status != 2) {
            req.setAttribute("code", "error");
            req.setAttribute("msg", "状态不对，不能发货！");
            return "f:/adminjsps/msg.jsp";
        }
        orderService.updateStatus(oid, 3);//设置状态为取消！
        req.setAttribute("code", "success");
        req.setAttribute("msg", "您的订单已发货");
        return "f:/adminjsps/msg.jsp";
    }
}