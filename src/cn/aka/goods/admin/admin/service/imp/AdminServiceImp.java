package cn.aka.goods.admin.admin.service.imp;

import cn.aka.goods.admin.admin.dao.imp.AdminDaoimp;
import cn.aka.goods.admin.admin.dao.AdminDao;
import cn.aka.goods.admin.admin.domain.Admin;
import cn.aka.goods.admin.admin.service.AdminService;

public class AdminServiceImp implements AdminService {
    private AdminDao adminDao = new AdminDaoimp();
    @Override
    public Admin login(Admin admin) {
        return adminDao.login(admin.getAdminname(),admin.getAdminpwd());
    }
}
