package cn.aka.goods.user.user.servlet;

import cn.aka.goods.user.user.domain.User;
import cn.aka.goods.user.user.exception.UserException;
import cn.aka.goods.user.user.service.UserService;
import cn.aka.goods.user.user.service.imp.UserServiceImp;
import cn.aka.goods.utils.BaseServlet;
import cn.itcast.commons.CommonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/user/userServlet")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserServiceImp();

    public String regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User fromUser = CommonUtils.toBean(request.getParameterMap(), User.class);
/**
 * 校验
 */
        Map<String, String> errors = validateRegist(fromUser, request.getSession());
        if (errors.size() > 0) {
            request.setAttribute("form", fromUser);
            request.setAttribute("errors", errors);
            return "f:/jsps/user/regist.jsp";
        }
        userService.regist(fromUser);
        request.setAttribute("code", "success");
        request.setAttribute("msg", "注册成功，请登录！");
        return "f:/jsps/msg.jsp";
    }

    /*
     * 注册校验
     */
    private Map<String, String> validateRegist(User formUser, HttpSession session) {
        Map<String, String> errors = new HashMap<String, String>();
        /*
         * 校验登录名
         */
        String loginname = formUser.getLoginname();
        if (loginname == null || loginname.trim().isEmpty()) {
            errors.put("loginname", "用户名不能为空！");
        } else if (loginname.length() < 3 || loginname.length() > 20) {
            errors.put("loginname", "用户名长度必须在3~20之间！");
        } else if (!userService.ajaxValidateLoginname(loginname)) {
            errors.put("loginname", "用户名已注册！");
        }

        /*
         * 校验密码
         */
        String loginpass = formUser.getLoginpass();
        if (loginpass == null || loginpass.trim().isEmpty()) {
            errors.put("loginpass", "密码不能为空！");
        } else if (loginpass.length() < 3 || loginpass.length() > 20) {
            errors.put("loginpass", "密码长度必须在3~20之间！");
        }

        /*
         * 确认密码校验
         */
        String reloginpass = formUser.getReloginpass();
        if (reloginpass == null || reloginpass.trim().isEmpty()) {
            errors.put("reloginpass", "确认密码不能为空！");
        } else if (!reloginpass.equals(loginpass)) {
            errors.put("reloginpass", "两次输入不一致！");
        }

        /*
         * 校验email
         */
        String email = formUser.getEmail();
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email不能为空！");
        } else if (!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
            errors.put("email", "Email格式错误！");
        } else if (!userService.ajaxValidateEmail(email)) {
            errors.put("email", "该Email已注册！");
        }

        /*
         * 验证码校验
         */
        String verifyCode = formUser.getVerifyCode();
        String vcode = (String) session.getAttribute("vCode");
        if (verifyCode == null || verifyCode.trim().isEmpty()) {
            errors.put("verifyCode", "验证码不能为空！");
        } else if (!verifyCode.equalsIgnoreCase(vcode)) {
            errors.put("verifyCode", "验证码错误！");
        }

        return errors;
    }

    /**
     * 状态激活
     */
    public void activation(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String code = req.getParameter("activationCode");
        try {
            userService.activation(code);
            req.setAttribute("code", "success");
            req.setAttribute("msg", "恭喜，激活成功!");
        } catch (UserException e) {
            req.setAttribute("msg", e.getMessage());
            req.setAttribute("code", "error");
        }
        req.getRequestDispatcher("/jsps/msg.jsp").forward(req, resp);
    }

    /**
     * 用户名是否注册
     */
    public String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /*
         * 获取用户名
         */
        String loginname = req.getParameter("loginname");
        /*
         * 通过service得到校验结果
         */
        boolean b = userService.ajaxValidateLoginname(loginname);
        /*
         * 发给客户端
         */
        resp.getWriter().print(b);
        return null;
    }

    /**
     * ajax Email是否注册校验
     */
    public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /*
         * 获取Email
         */
        String email = req.getParameter("email");
        /*
         * 通过service得到校验结果
         */
        boolean b = userService.ajaxValidateEmail(email);
        /*
         * 发给客户端
         */
        resp.getWriter().print(b);
        return null;
    }

    /**
     * ajax验证码是否正确校验
     */
    public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /*
         *  获取输入框中的验证码
         */
        String verifyCode = req.getParameter("verifyCode");
        /*
         *  获取图片上真实的校验码
         */
        String vcode = (String) req.getSession().getAttribute("vCode");
        /*
         *  进行忽略大小写比较，得到结果
         */
        boolean b = verifyCode.equalsIgnoreCase(vcode);
        /*
         *  发送给客户端
         */
        resp.getWriter().print(b);
        return null;
    }

    /**
     * 登陆
     */
    public String login(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
        Map<String, String> errors = validateLogin(formUser, req.getSession());
        if (errors.size() > 0) {
            req.setAttribute("form", formUser);
            req.setAttribute("errors", errors);
            return "f:/jsps/user/login.jsp";
        }

        User user = userService.login(formUser);
        if(user == null) {
            req.setAttribute("msg", "用户名或密码错误！");
            req.setAttribute("user", formUser);
            return "f:/jsps/user/login.jsp";
        } else {
            if(!user.isStatus()) {
                req.setAttribute("msg", "您还没有激活！");
                req.setAttribute("user", formUser);
                return "f:/jsps/user/login.jsp";
            } else {
                // 保存用户到session
                req.getSession().setAttribute("sessionUser", user);
                // 获取用户名保存到cookie中
                String loginname = user.getLoginname();
                loginname = URLEncoder.encode(loginname, "utf-8");
                Cookie cookie = new Cookie("loginname", loginname);
                cookie.setMaxAge(60 * 60 * 24 * 10);//保存10天
                resp.addCookie(cookie);
                return "r:/index.jsp";//重定向到主页
            }
        }
    }


    /*
     * 登录校验方法
     */
    private Map<String, String> validateLogin(User formUser, HttpSession session) {
        Map<String, String> errors = new HashMap<String, String>();
        /**
         * 用户名校验
         */
        String loginname = formUser.getLoginname();
        if(loginname==null||loginname.trim().isEmpty()){
            errors.put("loginname","用户名不能为空");
        }else if(loginname.length()<3||loginname.length()>20){
            errors.put("loginname", "用户名长度必须在3~20之间！");
        }

        /*
         * 校验密码
         */
        String loginpass = formUser.getLoginpass();
        if (loginpass == null || loginpass.trim().isEmpty()) {
            errors.put("loginpass", "密码不能为空！");
        } else if (loginpass.length() < 3 || loginpass.length() > 20) {
            errors.put("loginpass", "密码长度必须在3~20之间！");
        }

        /*
         * 验证码校验
         */
        String verifyCode = formUser.getVerifyCode();
        String vcode = (String) session.getAttribute("vCode");
        if (verifyCode == null || verifyCode.trim().isEmpty()) {
            errors.put("verifyCode", "验证码不能为空！");
        } else if (!verifyCode.equalsIgnoreCase(vcode)) {
            errors.put("verifyCode", "验证码错误！");
        }

        return errors;
    }

    /**
     * 修改密码
     */
    public String updatePassword(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
        User user = (User)req.getSession().getAttribute("sessionUser");
        // 用户未登录
        if(user == null) {
            req.setAttribute("msg", "您还没有登录！");
            return "f:/jsps/user/login.jsp";
        }
        try {
            userService.updatePassword(user.getUid(), formUser.getNewpass(), formUser.getLoginpass());
            req.setAttribute("msg", "修改密码成功");
            req.setAttribute("code", "success");
            return "f:/jsps/msg.jsp";
        } catch (UserException e) {
            req.setAttribute("msg", e.getMessage());//保存异常信息到request
            req.setAttribute("user", formUser);
            return "f:/jsps/user/pwd.jsp";
        }
    }

    /**
     * 退出
     */
    public String quit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getSession().invalidate();
        return "r:/jsps/user/login.jsp";
    }
}
