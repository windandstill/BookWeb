package cn.aka.goods.user.cartitem.dao;

import cn.aka.goods.user.cartitem.domain.CartItem;

import java.sql.SQLException;
import java.util.List;

public interface CartItemDao {

    /**
     * 透过购物车查询购物车列表
     */
    public List<CartItem> findByUser (String uid);

    /**
     * 查询某个用户的某本图书的购物车条目是否存在
     */
    public CartItem findByUidAndBid(String uid,String bid);

    /**
     * 修改购物车指定条目的数量
     */
    public void updateQuantity(String cartItemId,int quantity);

    /**
     * 添加购物车条目
     */
    public void addCartItem (CartItem cartItem);

    /**
     * 通过购物id删除购物车内容
     * @param cartItemIds
     */
    public void batchDelete(String cartItemIds);

    /**
     * 按id查询
     */
    CartItem findByCartItemId(String cartItemId);

    /**
     * 加载多个CartItem
     */
    List<CartItem> loadCartItems(String cartItemIds);
}
