package cn.aka.goods.user.cartitem.service;

import cn.aka.goods.user.cartitem.dao.CartItemDao;
import cn.aka.goods.user.cartitem.dao.imp.CartItemDaoImp;
import cn.aka.goods.user.cartitem.domain.CartItem;
import cn.aka.goods.user.cartitem.service.imp.CartItemServiceImp;

import java.util.List;

public interface CartItemService {
    /**
     * 根据uid返回购物车内容
     * @param uid
     * @return
     */
    public List<CartItem> myCart(String uid);

    /**
     * 购物车添加物品
     * 1.使用uid和bid去数据库查这个条目是否存在
     * 2.如果没有条目就添加条目 如果有就修改条目
     */
    public void add(CartItem cartItem);

    /**
     * 通过购物id删除购物车内容
     * @param cartItemIds
     */
    void batchDelete(String cartItemIds);

    /**
     * 修改条目数量
     */
    CartItem updateQuantity(String cartItemId, int quantity);
}
