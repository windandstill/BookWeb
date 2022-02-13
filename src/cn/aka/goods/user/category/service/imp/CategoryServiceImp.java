package cn.aka.goods.user.category.service.imp;


import cn.aka.goods.user.category.dao.imp.CategoryDaoImp;
import cn.aka.goods.user.category.domain.Category;
import cn.aka.goods.user.category.service.CategoryService;

import java.util.List;

public class CategoryServiceImp implements CategoryService {
    private CategoryDaoImp categoryDao = new CategoryDaoImp();

    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    public void add(Category category) {
        categoryDao.add(category);
    }

    public List<Category> findParents() {
        return categoryDao.findParents();
    }

    public Category load(String cid) {
        return categoryDao.load(cid);
    }

    public void edit(Category category) {
        categoryDao.edit(category);
    }

    public int findChildrenCountByParent(String cid) {
        return categoryDao.findChildrenCountByParent(cid);
    }

    public void delete(String cid){
        categoryDao.delete(cid);
    }
}
