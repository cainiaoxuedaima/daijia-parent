package cn.van.daijia.driver.service;

import cn.van.daijia.model.vo.order.NewOrderDataVo;

import java.util.List;

public interface OrderService {


    // 获取订单状态
    Integer getOrderStatus(Long orderId);

    // 添加并开始新订单任务调度
    List<NewOrderDataVo> findNewOrderQueueData(Long driverId);
}
