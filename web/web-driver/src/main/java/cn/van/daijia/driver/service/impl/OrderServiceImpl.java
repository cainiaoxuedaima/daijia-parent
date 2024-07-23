package cn.van.daijia.driver.service.impl;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.dispatch.client.NewOrderFeignClient;
import cn.van.daijia.driver.service.OrderService;
import cn.van.daijia.model.vo.order.NewOrderDataVo;
import cn.van.daijia.order.client.OrderInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;
    //获取订单状态
    @Override
    public Integer getOrderStatus(Long orderId) {
        Result<Integer> orderStatus = orderInfoFeignClient.getOrderStatus(orderId);

        return orderStatus.getData();
    }

    @Autowired
    private NewOrderFeignClient newOrderFeignClient;
    /**
     * 获取司机新订单队列数据
     * @param driverId
     * @return
     */
    @Override
    public List<NewOrderDataVo> findNewOrderQueueData(Long driverId) {
        return newOrderFeignClient.findNewOrderQueueData(driverId).getData();
    }
}
