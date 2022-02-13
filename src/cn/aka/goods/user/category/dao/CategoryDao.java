package cn.aka.goods.user.category.dao;

import cn.aka.goods.user.category.domain.Category;
import cn.itcast.commons.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CategoryDao {

    /**
     * 查询所有分类
     */
    public List<Category> findAll();

    public List<Category> findByParent(String pid);


    public void add(Category category);

    /**
     * 获取一级分类
     * @return
     */
    public List<Category> findParents();

    /**
     * 加载分类
     */
    public Category load(String cid);

    /**
     * 修改分类
     */
    public void edit(Category category);

    /**
     *查询子分类个数
     */
    public int findChildrenCountByParent(String cid);

    /**
     * 删除分类
     * @param cid
     */
    public void delete(String cid);
}
