package cn.van.daijia.order.service;

import cn.van.daijia.model.entity.order.OrderInfo;
import cn.van.daijia.model.form.order.OrderInfoForm;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderInfoService extends IService<OrderInfo> {

    //保存订单信息
    Long saveOrderInfo(OrderInfoForm orderInfoForm);
}
