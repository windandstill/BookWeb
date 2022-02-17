package cn.aka.goods.user.cartitem.dao;

import cn.aka.goods.user.cartitem.domain.CartItem;

import java.util.List;

public interface CartItemDao {

    /**
     * 透过购物车查询购物车列表
     */
    public List<CartItem> findByUser (String uid);
}
