package cn.aka.goods.user.category.service;

import cn.aka.goods.user.category.domain.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll();

    public void add(Category category);

    public List<Category> findParents();

    public Category load(String cid);
    public void edit(Category category);

    public int findChildrenCountByParent(String cid);

    public void delete(String cid);
}
