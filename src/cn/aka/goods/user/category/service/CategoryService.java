package cn.aka.goods.user.category.service;

import cn.aka.goods.user.category.domain.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll();

    public void add(Category category);

    /**
     * 查询所有父分类
     * @return
     */
    public List<Category> findParents();

    /**
     * 查询父分类下的子分类
     * @param pid
     * @return
     */
    public  List<Category> findChildren(String pid);

    public Category load(String cid);

    public void edit(Category category);

    public int findChildrenCountByParent(String cid);

    public void delete(String cid);
}
