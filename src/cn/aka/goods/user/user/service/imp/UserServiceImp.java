package cn.aka.goods.user.user.service.imp;

import cn.aka.goods.user.user.dao.UserDao;
import cn.aka.goods.user.user.dao.imp.UserDaoImp;
import cn.aka.goods.user.user.domain.User;
import cn.aka.goods.user.user.exception.UserException;
import cn.aka.goods.user.user.service.UserService;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

public class UserServiceImp implements UserService {
    private UserDao userDao = new UserDaoImp();


    /**
     * 用户名注册校验
     */
    public boolean ajaxValidateLoginname(String loginname) {
        return userDao.ajaxValidateLoginname(loginname);
    }

    /**
     * Email校验
     */
    public boolean ajaxValidateEmail(String email) {
        return userDao.ajaxValidateEmail(email);
    }

    @Override
    public void regist(User user) {
        user.setUid(CommonUtils.uuid());
        user.setStatus(false);
        user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());
        userDao.add(user);

        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 登陆邮件服务器
         */
        String host = prop.getProperty("host");
        String name = prop.getProperty("username");
        String pass = prop.getProperty("password");
        Session session = MailUtils.createSession(host, name, pass);

        /**
         * 创建Mail
         */
        String from = prop.getProperty("from");
        String to = user.getEmail();
        String subject = prop.getProperty("subject");
        String content = MessageFormat.format(prop.getProperty("content"),user.getActivationCode());
        Mail mail = new Mail(from, to, subject, content);
        try {
            MailUtils.send(session, mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 激活
     */
    @Override
    public void activation(String code) throws UserException {
        User user = userDao.findByCode(code);
        if(user==null) throw  new UserException("无效的激活码!");
        if(user.isStatus()) throw new UserException("您已经完成激活");
        userDao.updateStatus(user.getUid(),true);
    }

    /**
     * 登陆
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
    }
}
