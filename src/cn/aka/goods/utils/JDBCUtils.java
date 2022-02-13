package cn.aka.goods.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {
    //定义成员变量 DateSource
    private static DataSource ds;
    static {
        try {
            //加载配置文件
        Properties properties = new Properties();
        properties.load(JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties"));
        //获取DataSource
            ds= DruidDataSourceFactory.createDataSource(properties);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取连接对象
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * 释放资源
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (conn != null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public static void close(Statement stmt,Connection conn){
        close(null,stmt,conn);
    }

    /**
     * 获取连接池
     */
    public static DataSource getDataSource(){
        return ds;
    }

    private static ThreadLocal<Connection>  tl=new ThreadLocal<Connection>();
    /**
     *
     * @return
     * @throws SQLException
     * 返回连接，首先判断线程是否含有连接，如果没有，则新建连接
     */

    /**
     * 开启事务
     * @throws SQLException
     */
    public static void beginTransaction() throws SQLException{
        Connection con=tl.get();
        if(con!=null) throw new RuntimeException("已经开启了事务");
        con=getConnection();
        con.setAutoCommit(false);
        tl.set(con);
    }
    /**
     * @throws SQLException
     *提交事务
     */
    public static void commitTransaction() throws SQLException{
        Connection con=tl.get();
        if(con==null) throw new RuntimeException("没有事务");
        con.commit();
        con.close();
        tl.remove();
    }
    /**
     * @throws SQLException
     * 回滚事务
     */
    public static void rollbackTransaction() throws SQLException{
        Connection con=tl.get();
        if(con==null) throw new RuntimeException("没有事务");
        con.rollback();
        con.close();
        tl.remove();
    }
    /**
     *
     * @param connection
     * @throws SQLException
     * 判断是否能够关闭连接，如果不属于事务那么就关闭。
     */
    public static void releaseConnection(Connection connection) throws SQLException{
        Connection con=tl.get();
        if(con==null) connection.close();
        if(con!=connection) connection.close();
    }
    /**
     *
     * @return
     * 返回连接池对象
     */
    public static DataSource getDateSource(){
        return ds;

    }
}
