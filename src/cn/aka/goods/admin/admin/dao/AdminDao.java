package cn.aka.goods.admin.admin.dao;

import cn.aka.goods.admin.admin.domain.Admin;

public interface AdminDao {
    public Admin login(String adminname, String adminpwd);
}
