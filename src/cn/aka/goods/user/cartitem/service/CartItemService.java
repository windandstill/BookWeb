package cn.aka.goods.user.cartitem.service;

import cn.aka.goods.user.cartitem.dao.CartItemDao;
import cn.aka.goods.user.cartitem.dao.imp.CartItemDaoImp;
import cn.aka.goods.user.cartitem.domain.CartItem;
import cn.aka.goods.user.cartitem.service.imp.CartItemServiceImp;

import java.util.List;

public interface CartItemService {
    public List<CartItem> myCart(String uid);
}
