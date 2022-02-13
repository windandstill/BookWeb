package cn.aka.goods.admin.admin.dao.imp;

import cn.aka.goods.admin.admin.dao.AdminDao;
import cn.aka.goods.admin.admin.domain.Admin;
import cn.aka.goods.utils.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class AdminDaoimp implements AdminDao {
    private JdbcTemplate template=  new JdbcTemplate(JDBCUtils.getDataSource());;

    @Override
    public Admin login(String adminname, String adminpwd) {
        String sql = "select * from t_admin where adminname=? and adminpwd=?";
        List<Admin> query = template.query(sql,new BeanPropertyRowMapper<Admin>(Admin.class),adminname,adminpwd);
        for (Admin admin:query){
            return admin;
        }
        return null;
    }
}
