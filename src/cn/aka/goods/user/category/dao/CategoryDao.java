package cn.aka.goods.user.category.dao;

import cn.aka.goods.user.category.domain.Category;
import cn.aka.goods.utils.JDBCUtils;
import cn.itcast.commons.CommonUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CategoryDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 将map中的数据封装为Category对象
     *
     * @param map
     * @return Category对象
     */
    private Category toCategory(Map<String, Object> map) {
        Category category = CommonUtils.toBean(map, Category.class);//将此方法可以放入JdbcTemplate
        String pid = (String) map.get("pid");
        if (pid != null) {
            Category parent = new Category();
            parent.setCid(pid);
            category.setParent(parent);
        }
        return category;
    }

    /**
     * 多个map数据映射到Category
     *
     * @param maps
     * @return Category对象集合
     */
    private List<Category> toCategoryList(List<Map<String, Object>> maps) {
        List<Category> categoryList = new ArrayList<Category>();
        for (Map<String, Object> map : maps) {
            Category c = toCategory(map);
            categoryList.add(c);
        }
        return categoryList;
    }

    /**
     * 查询所有分类
     */
    public List<Category> findAll() {
        String sql = "select * from goods.t_category where pid is null order by orderBy";
        List<Map<String, Object>> maps = template.queryForList(sql);
        List<Category> parents = toCategoryList(maps);
        for (Category parent:parents){
            List<Category> children = findByParent(parent.getCid());
            parent.setChildren(children);
        }
        return parents;
    }

    public List<Category> findByParent(String pid){
        String sql = "select * from goods.t_category where pid =? order by orderBy";
        List<Map<String, Object>> maps = template.queryForList(sql,pid);
        return toCategoryList(maps);
    }
}
