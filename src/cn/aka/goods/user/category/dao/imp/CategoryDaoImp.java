package cn.aka.goods.user.category.dao.imp;

import cn.aka.goods.user.category.dao.CategoryDao;
import cn.aka.goods.user.category.domain.Category;
import cn.aka.goods.utils.JDBCUtils;
import cn.itcast.commons.CommonUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CategoryDaoImp implements CategoryDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 将map中的数据封装为Category对象
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
    @Override
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

    /**
     * 通过父类查询子类
     */
    @Override
    public List<Category> findByParent(String pid){
        String sql = "select * from goods.t_category where pid =? order by orderBy";
        List<Map<String, Object>> maps = template.queryForList(sql,pid);
        return toCategoryList(maps);
    }


    @Override
    public void add(Category category){
        String sql = "insert into goods.t_category(cid,cname,pid,`desc`) values (?,?,?,?)";
        String pid = null;//初始一级分类
        if(category.getParent()!=null){
            pid= category.getParent().getCid();
        }
        Object[] params = {category.getCid(),category.getCname(),pid,category.getDesc()};
        template.update(sql,params);
    }

    /**
     * 获取一级分类
     * @return
     */
    @Override
    public List<Category> findParents() {
        String sql = "select * from goods.t_category where pid is null order by orderBy";
        List<Map<String, Object>> maps = template.queryForList(sql);
        List<Category> parents = toCategoryList(maps);
        return parents;
    }

    /**
     * 加载分类
     */
    @Override
    public Category load(String cid){
        String sql = "select * from goods.t_category where cid = ?";
        return toCategory(template.queryForMap(sql,cid));
    }

    /**
     * 修改分类
     */
    @Override
    public void edit(Category category){
        String sql="update goods.t_category set cname=?,pid=?,`desc`=? where cid =?";
        String pid = null;
        if(category.getParent()!=null){
            pid = category.getParent().getCid();
        }
        Object[] params = {category.getCname(),pid,category.getDesc(),category.getCid()};
        template.update(sql,params);
    }

    /**
     *查询子分类个数
     */
    @Override
    public int findChildrenCountByParent(String cid){
        String sql = "select count(*) from goods.t_category where pid = ?";
        Long total = template.queryForObject(sql, Long.class, cid);
        return total == null ? 0 : total.intValue();
    }

    /**
     * 删除分类
     * @param cid
     */
    @Override
    public void delete(String cid){
        String sql = "delete from goods.t_category where cid = ?";
        template.update(sql,cid);
    }
}
