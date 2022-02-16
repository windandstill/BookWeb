package cn.aka.goods.user.cartitem.service.imp;

import cn.aka.goods.user.cartitem.dao.CartItemDao;
import cn.aka.goods.user.cartitem.dao.imp.CartItemDaoImp;
import cn.aka.goods.user.cartitem.service.CartItemService;

public class CartItemServiceImp implements CartItemService {
    private CartItemDao cartItemDao = new CartItemDaoImp();
    /*private CartItemService cartItemService =new CartItemServiceImp();*/

}
