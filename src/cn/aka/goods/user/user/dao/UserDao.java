package cn.aka.goods.user.user.dao;

import cn.aka.goods.user.user.domain.User;

import java.sql.SQLException;
import java.util.Map;

public interface UserDao {
    /**
     * 校验用户名是否注册n
     */
    boolean ajaxValidateLoginname(String loginname);

    /**
     * 校验Email是否注册
     */
    boolean ajaxValidateEmail(String email);

    /**
     * 添加用户
     */
    void add(User user);

    /**
     * 通过激活码查询用户
     */
    User findByCode(String code);

    /**
     * 修改状态
     */
    void updateStatus(String uid,boolean status);

    User findByLoginnameAndLoginpass(String loginname, String loginpass);
}
