package cn.aka.goods.user.cartitem.Servlet;

import cn.aka.goods.user.cartitem.domain.CartItem;
import cn.aka.goods.user.cartitem.service.CartItemService;
import cn.aka.goods.user.cartitem.service.imp.CartItemServiceImp;
import cn.aka.goods.user.user.domain.User;
import cn.aka.goods.utils.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet("/user/cartItemServlet")
public class CartItemServlet extends BaseServlet {
    private CartItemService cartItemService =new CartItemServiceImp();

    public String myCart(HttpServletRequest req, HttpServletResponse resp){
        //1.得到uid
        User user = (User) req.getSession().getAttribute("sessionUser");
        String uid = user.getUid();
        //2.调用service查询
        List<CartItem> cartItemList = cartItemService.myCart(uid);
        req.setAttribute("cartItemList",cartItemList);
        return "f:/jsps/cart/list.jsp";
    }
}
