package cn.aka.goods.user.category.service.imp;


import cn.aka.goods.user.category.dao.imp.CategoryDaoImp;
import cn.aka.goods.user.category.domain.Category;
import cn.aka.goods.user.category.service.CategoryService;

import java.util.List;

public class CategoryServiceImp implements CategoryService {
    private CategoryDaoImp categoryDao = new CategoryDaoImp();

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public void add(Category category) {
        categoryDao.add(category);
    }

    @Override
    public List<Category> findParents() {
        return categoryDao.findParents();
    }

    @Override
    public List<Category> findChildren(String pid) {
        return categoryDao.findByParent(pid);
    }

    @Override
    public Category load(String cid) {
        return categoryDao.load(cid);
    }

    @Override
    public void edit(Category category) {
        categoryDao.edit(category);
    }

    @Override
    public int findChildrenCountByParent(String cid) {
        return categoryDao.findChildrenCountByParent(cid);
    }

    @Override
    public void delete(String cid){
        categoryDao.delete(cid);
    }
}
