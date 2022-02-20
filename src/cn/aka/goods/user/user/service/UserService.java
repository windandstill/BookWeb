package cn.aka.goods.user.user.service;

import cn.aka.goods.user.user.domain.User;
import cn.aka.goods.user.user.exception.UserException;

import java.util.Map;

public interface UserService {
    boolean ajaxValidateLoginname(String loginname);
    boolean ajaxValidateEmail(String email);
    void regist(User user);
    void activation(String code) throws UserException;
    User login(User user);
    void updatePassword(String uid,String newPass,String oldPass) throws UserException;
}
