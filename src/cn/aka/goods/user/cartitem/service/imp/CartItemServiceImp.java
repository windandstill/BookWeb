package cn.aka.goods.user.cartitem.service.imp;

import cn.aka.goods.user.cartitem.dao.CartItemDao;
import cn.aka.goods.user.cartitem.dao.imp.CartItemDaoImp;
import cn.aka.goods.user.cartitem.domain.CartItem;
import cn.aka.goods.user.cartitem.service.CartItemService;

import java.util.List;

public class CartItemServiceImp implements CartItemService {
    private CartItemDao cartItemDao = new CartItemDaoImp();

    @Override
    public List<CartItem> myCart(String uid){
        return cartItemDao.findByUser(uid);
    }


}
