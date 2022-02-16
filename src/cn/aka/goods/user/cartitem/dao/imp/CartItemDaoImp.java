package cn.aka.goods.user.cartitem.dao.imp;

import cn.aka.goods.user.cartitem.dao.CartItemDao;
import cn.aka.goods.utils.JDBCUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class CartItemDaoImp implements CartItemDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
}
