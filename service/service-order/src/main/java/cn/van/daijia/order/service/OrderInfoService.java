package cn.van.daijia.order.service;

import cn.van.daijia.model.entity.order.OrderInfo;
import cn.van.daijia.model.form.order.OrderInfoForm;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderInfoService extends IService<OrderInfo> {

    //保存订单信息
    Long saveOrderInfo(OrderInfoForm orderInfoForm);
    //获取订单状态
    Integer getOrderStatus(Long orderId);

    //司机抢单
    Boolean robNewOrder(Long driverId, Long orderId);
}
