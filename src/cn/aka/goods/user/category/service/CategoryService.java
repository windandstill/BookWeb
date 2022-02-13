package cn.aka.goods.user.category.service;


import cn.aka.goods.user.category.dao.CategoryDao;
import cn.aka.goods.user.category.domain.Category;

import java.util.List;

public class CategoryService {
    private CategoryDao categoryDao = new CategoryDao();

    public List<Category> findAll(){
        return categoryDao.findAll();
    }
}
