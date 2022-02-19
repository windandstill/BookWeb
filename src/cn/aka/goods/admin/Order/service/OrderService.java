package cn.aka.goods.admin.Order.service;

import cn.aka.goods.admin.Order.dao.OrderDao;
import cn.aka.goods.user.order.domain.Order;
import cn.aka.goods.user.pager.PageBean;
import cn.aka.goods.utils.JDBCUtils;



import java.sql.SQLException;
import java.util.Map;

public class OrderService {
    private OrderDao orderDao = new OrderDao();

    /**
     * 修改订单状态
     * @param oid
     * @param status
     */
    public void updateStatus(String oid, int status) {
        try {
            orderDao.updateStatus(oid, status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询订单状态
     * @param oid
     * @return
     */
    public int findStatus(String oid) {
        try {

            return orderDao.findStatus(oid);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载订单
     * @param oid
     * @return
     */
    public Order load(String oid) throws SQLException {
        return orderDao.load(oid);
//        try {
//            JDBCUtils.beginTransaction();
//            Order order = orderDao.load(oid);
//            JDBCUtils.commitTransaction();
//            return order;
//        } catch (SQLException e) {
//            try {
//                JDBCUtils.rollbackTransaction();
//            } catch (SQLException e1) {}
//            throw new RuntimeException(e);
//        }
    }

    /**
     * 生成订单
     * @param order
     */
    public void createOrder(Order order) {
        try {
            orderDao.add(order);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
//
////        try {
////            JDBCUtils.beginTransaction();
////            orderDao.add(order);
////            JDBCUtils.commitTransaction();
////        } catch (SQLException e) {
////            try {
////                JDBCUtils.rollbackTransaction();
////            } catch (SQLException e1) {}
////            throw new RuntimeException(e);
////        }
//    }

    /**
     * 我的订单
     * @param uid
     * @param pageNow
     * @return
     */
    public PageBean<Order> myOrders(String uid, int pageNow) throws SQLException {
        return orderDao.findByUser(uid,pageNow);
    }
    /*try {
            JDBCUtils.beginTransaction();
            PageBean<Order> pb = orderDao.findByUser(uid, pageNow);
            JDBCUtils.commitTransaction();
            return pb;
        } catch (SQLException e) {
           try {
               JDBCUtils.rollbackTransaction();
            } catch (SQLException e1) {}
            throw new RuntimeException(e);
       }
    }*/

    /**
     * 按状态查询
     * @param status
     * @param pageNow
     * @return
     */
    public PageBean<Order> findByStatus(int status, int pageNow) throws SQLException {
        return  orderDao.findByStatus(status,pageNow);
//        try {
//            JDBCUtils.beginTransaction();
//            PageBean<Order> pb = orderDao.findByStatus(status, pageNow);
//            JDBCUtils.commitTransaction();
//            return pb;
//        } catch (SQLException e) {
//            try {
//                JDBCUtils.rollbackTransaction();
//            } catch (SQLException e1) {}
//            throw new RuntimeException(e);
//        }
    }

    /**
     * 查询所有
     * @param pageNow
     * @return
     */
    public PageBean<Order> findAll(int pageNow) throws SQLException {
        return orderDao.findAll(pageNow);
//        try {
//            JDBCUtils.beginTransaction();
//            PageBean<Order> pb = orderDao.findAll(pageNow);
//            JDBCUtils.commitTransaction();
//            return pb;
//        } catch (SQLException e) {
//            try {
//                JDBCUtils.rollbackTransaction();
//            } catch (SQLException e1) {}
//            throw new RuntimeException(e);
//        }
    }
}
