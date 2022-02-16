package cn.aka.goods.user.cartitem.Servlet;

import cn.aka.goods.user.cartitem.service.CartItemService;
import cn.aka.goods.user.cartitem.service.imp.CartItemServiceImp;
import cn.aka.goods.utils.BaseServlet;

public class CartItemServlet extends BaseServlet {
    private CartItemService cartItemService =new CartItemServiceImp();

}
