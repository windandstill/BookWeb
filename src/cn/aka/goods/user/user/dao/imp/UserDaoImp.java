package cn.aka.goods.user.user.dao.imp;

import cn.aka.goods.user.user.dao.UserDao;
import cn.aka.goods.user.user.domain.User;
import cn.aka.goods.utils.JDBCUtils;
import cn.itcast.jdbc.TxQueryRunner;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserDaoImp implements UserDao {


        private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

        /**
         * 按uid和password查询
         */
        @Override
        public boolean findByUidAndPassword(String uid, String password){
            String sql = "select count(*) from goods.t_user where uid=? and loginpass=?";
            Long number = template.queryForObject(sql, Long.class, uid, password);
            return number.intValue() > 0;
        }

        /**
         * 修改密码
         */
        public void updatePassword(String uid, String password) {
            String sql = "update goods.t_user set loginpass=? where uid=?";
            template.update(sql, password, uid);
        }


        /**
         * 校验用户名是否注册
         */
        @Override
        public boolean ajaxValidateLoginname(String loginname)  {
            String sql = "select count(1) from goods.t_user where loginname=?";
            Long number = template.queryForObject(sql, Long.class, loginname);
            return number.intValue() == 0;
        }

        /**
         * 校验Email是否注册
         */
        @Override
        public boolean ajaxValidateEmail(String email) {
            String sql = "select count(1) from goods.t_user where email=?";
            Long number = template.queryForObject(sql, Long.class, email);
            return number.intValue() == 0;
        }

    /**
     * 添加用户
     */
    @Override
    public void add(User user) {
        String sql = "insert into goods.t_user values(?,?,?,?,?,?)";
            Object[] params = {user.getUid(), user.getLoginname(), user.getLoginpass(),
                    user.getEmail(), 1, user.getActivationCode()};
            template.update(sql, params);
    }

    /**
     * 通过激活码查询用户
     */
    @Override
    public User findByCode(String code) {
        String sql = "select * from goods.u_user where activationCode = ?";
        User user = template.queryForObject(sql, User.class, code);
        return user;
    }

    /**
     * 更新状态
     */
    @Override
    public void updateStatus(String uid, boolean status) {
        String sql = "update goods.t_user set status = ? where uid = ?";
        template.update(sql,status,uid);
    }

    @Override
    public User findByLoginnameAndLoginpass(String loginname, String loginpass) {
        String sql = "select * from goods.t_user where loginname = ? and loginpass = ?";
        List<User> userlist = template.query(sql, new BeanPropertyRowMapper<>(User.class), loginname, loginpass);
        for (User user:userlist){
            return user;
        }
        return null;
    }


}
