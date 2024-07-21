package cn.van.daijia.order.service.impl;

import cn.van.daijia.model.entity.order.OrderInfo;
import cn.van.daijia.model.entity.order.OrderStatusLog;
import cn.van.daijia.model.enums.OrderStatus;
import cn.van.daijia.model.form.order.OrderInfoForm;
import cn.van.daijia.order.mapper.OrderStatusLogMapper;
import cn.van.daijia.order.service.OrderInfoService;
import cn.van.daijia.order.mapper.OrderInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private OrderStatusLogMapper orderStatusLogMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    //保存订单信息
    @Override
    public Long saveOrderInfo(OrderInfoForm orderInfoForm) {
        //
        OrderInfo orderInfo=new OrderInfo();
        BeanUtils.copyProperties(orderInfoForm,orderInfo);
        //订单号
        String orderNo= UUID.randomUUID().toString().replaceAll("-","");
        orderInfo.setOrderNo(orderNo);
        //订单状态
        orderInfo.setStatus(OrderStatus.WAITING_ACCEPT.getStatus());
        orderInfoMapper.insert(orderInfo);

        //记录日志
        this.log(orderInfo.getId(),orderInfo.getStatus());


        return orderInfo.getId();
    }

    public void log(Long orderId, Integer status) {
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        orderStatusLog.setOrderId(orderId);
        orderStatusLog.setOrderStatus(status);
        orderStatusLog.setOperateTime(new Date());
        orderStatusLogMapper.insert(orderStatusLog);
    }
}
