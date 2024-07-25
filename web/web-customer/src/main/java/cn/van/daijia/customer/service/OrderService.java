package cn.van.daijia.customer.service;

import cn.van.daijia.model.form.customer.ExpectOrderForm;
import cn.van.daijia.model.form.customer.SubmitOrderForm;
import cn.van.daijia.model.vo.customer.ExpectOrderVo;
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
}
