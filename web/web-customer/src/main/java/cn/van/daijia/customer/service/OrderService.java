package cn.van.daijia.customer.service;

import cn.van.daijia.model.form.customer.ExpectOrderForm;
import cn.van.daijia.model.form.customer.SubmitOrderForm;
import cn.van.daijia.model.form.map.CalculateDrivingLineForm;
import cn.van.daijia.model.vo.customer.ExpectOrderVo;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.map.DrivingLineVo;
import cn.van.daijia.model.vo.map.OrderServiceLastLocationVo;
import cn.van.daijia.model.vo.order.CurrentOrderInfoVo;
import cn.van.daijia.model.vo.order.OrderInfoVo;

public interface OrderService {

    // 预估订单
    ExpectOrderVo expectOrder(ExpectOrderForm expectOrderForm);

    // 提交订单
    Long submitOrder(SubmitOrderForm submitOrderForm);

    // 获取订单状态
    Integer getOrderStatus(Long orderId);

    // 乘客端查找当前订单
    CurrentOrderInfoVo searchCustomerCurrentOrder(Long customerId);

    //获取订单信息
    OrderInfoVo getOrderInfo(Long orderId, Long customerId);

    //根据订单id获取司机基本信息
    DriverInfoVo getDriverInfo(Long orderId, Long customerId);

    //计算最佳驾驶线路
    DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm);

    //代驾服务：获取订单服务最后一个位置信息
    OrderServiceLastLocationVo getOrderServiceLastLocation(Long orderId);
}
