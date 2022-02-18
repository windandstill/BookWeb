package cn.aka.goods.user.cartitem.service.imp;

import cn.aka.goods.user.cartitem.dao.CartItemDao;
import cn.aka.goods.user.cartitem.dao.imp.CartItemDaoImp;
import cn.aka.goods.user.cartitem.domain.CartItem;
import cn.aka.goods.user.cartitem.service.CartItemService;
import cn.itcast.commons.CommonUtils;

import java.util.List;

public class CartItemServiceImp implements CartItemService {
    private CartItemDao cartItemDao = new CartItemDaoImp();

    /**
     * 根据uid返回购物车内容
     * @param uid
     * @return
     */
    @Override
    public List<CartItem> myCart(String uid){
        return cartItemDao.findByUser(uid);
    }

    /**
     * 购物车添加物品
     * 1.使用uid和bid去数据库查这个条目是否存在
     * 2.如果没有条目就添加条目 如果有就修改条目
     */
    @Override
    public void add(CartItem cartItem){
        CartItem cartItem1 = cartItemDao.findByUidAndBid(cartItem.getUser().getUid(),cartItem.getBook().getBid());
        if (cartItem1 == null){
            cartItem.setCartItemId(CommonUtils.uuid());
            cartItemDao.addCartItem(cartItem);
        }else {
            int quantity = cartItem.getQuantity() + cartItem1.getQuantity();
            cartItemDao.updateQuantity(cartItem1.getCartItemId(),quantity);
        }
    }

    /**
     * 通过购物id删除购物车内容
     * @param cartItemIds
     */
    @Override
    public void batchDelete(String cartItemIds){
        cartItemDao.batchDelete(cartItemIds);
    }

    /**
     * 修改条目数量
     */
    @Override
    public CartItem updateQuantity(String cartItemId,int quantity){
        cartItemDao.updateQuantity(cartItemId,quantity);
        return cartItemDao.findByCartItemId(cartItemId);
    }
}
