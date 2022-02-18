package cn.aka.goods.user.cartitem.Servlet;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.cartitem.domain.CartItem;
import cn.aka.goods.user.cartitem.service.CartItemService;
import cn.aka.goods.user.cartitem.service.imp.CartItemServiceImp;
import cn.aka.goods.user.user.domain.User;
import cn.aka.goods.utils.BaseServlet;
import cn.itcast.commons.CommonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/cartItemServlet")
public class CartItemServlet extends BaseServlet {
    private CartItemService cartItemService =new CartItemServiceImp();

    /**
     * 通过uid返回用户购物车列表
     * @param req
     * @param resp
     * @return
     */
    public String myCart(HttpServletRequest req, HttpServletResponse resp){
        //1.得到uid
        User user = (User) req.getSession().getAttribute("sessionUser");
        String uid = user.getUid();
        //2.调用service查询
        List<CartItem> cartItemList = cartItemService.myCart(uid);
        req.setAttribute("cartItemList",cartItemList);
        return "f:/jsps/cart/list.jsp";
    }

    /**
     * 获取表单项添加购物车条目
     */
    public String add(HttpServletRequest req, HttpServletResponse resp){
        CartItem cartItem = CommonUtils.toBean(req.getParameterMap(),CartItem.class);
        cartItem.setUser((User) req.getSession().getAttribute("sessionUser"));
        Book book = CommonUtils.toBean(req.getParameterMap(),Book.class);
        cartItem.setBook(book);
        cartItemService.add(cartItem);
        return myCart(req,resp);
    }

    /**
     * 通过购物ids删除购物车内容
     */
    public String batchDelete(HttpServletRequest req, HttpServletResponse resp){
        String cartItemIds = req.getParameter("cartItemIds");
        cartItemService.batchDelete(cartItemIds);
        return myCart(req,resp);
    }

    /**
     * 修改条目数量
     */
    public String updateQuantity(HttpServletRequest req, HttpServletResponse resp) {
        String cartItemId = req.getParameter("cartItemId");
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        CartItem cartItem = cartItemService.updateQuantity(cartItemId, quantity);
        // 给客户端返回一个json对象
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
        sb.append(",");
        sb.append("\"subtotal\"").append(":").append(cartItem.getSubTotal());
        sb.append("}");

        try {
            resp.getWriter().print(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载多个CartItem
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String loadCartItems(HttpServletRequest req, HttpServletResponse resp) {
         //1. 获取cartItemIds参数
        String cartItemIds = req.getParameter("cartItemIds");
        double total = Double.parseDouble(req.getParameter("total"));
         //2. 通过service得到List<CartItem>
        List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
         //3. 保存，然后转发到/cart/showitem.jsp
        req.setAttribute("cartItemList", cartItemList);
        req.setAttribute("total", total);
        req.setAttribute("cartItemIds", cartItemIds);
        return "f:/jsps/cart/showitem.jsp";
    }
}
