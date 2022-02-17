package cn.aka.goods.user.cartitem.dao.imp;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.cartitem.dao.CartItemDao;
import cn.aka.goods.user.cartitem.domain.CartItem;
import cn.aka.goods.user.user.domain.User;
import cn.aka.goods.utils.JDBCUtils;
import cn.itcast.commons.CommonUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartItemDaoImp implements CartItemDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 将查询多表查询出来的map封装成多个对象
     * @param map
     * @return
     */
    private CartItem toCartIem(Map<String,Object> map){
        if(map == null) {
            return null;
        }
        CartItem cartItem = CommonUtils.toBean(map,CartItem.class);
        Book book = CommonUtils.toBean(map, Book.class);
        User user = CommonUtils.toBean(map,User.class);
        cartItem.setBook(book);
        cartItem.setUser(user);
        return cartItem;
    }

    /**
     * 将多个装有多个map的list映射成多个对象
     * @param mapList
     * @return
     */
    private List<CartItem> toCartItemList(List<Map<String, Object>> mapList){
        List<CartItem> cartItemList = new ArrayList<>();
        for (Map<String,Object> map : mapList){
            CartItem cartItem = toCartIem(map);
            cartItemList.add(cartItem);
        }
        return cartItemList;
    }

    /**
     * 通过购物车查询购物车列表
     */
    @Override
    public List<CartItem> findByUser(String uid) {
        String sql = "SELECT * FROM t_cartitem c, t_book b Where c.bid=b.bid and uid=? order by c.orderBy";
        List<Map<String, Object>> mapList = template.queryForList(sql, uid);
        return toCartItemList(mapList);
    }
}
