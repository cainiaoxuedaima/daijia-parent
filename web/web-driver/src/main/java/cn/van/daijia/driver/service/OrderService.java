package cn.van.daijia.driver.service;

public interface OrderService {


    // 获取订单状态
    Integer getOrderStatus(Long orderId);
}
