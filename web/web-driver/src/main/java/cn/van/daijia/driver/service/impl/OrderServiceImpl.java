package cn.van.daijia.driver.service.impl;

import cn.van.daijia.common.execption.GuiguException;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.result.ResultCodeEnum;
import cn.van.daijia.dispatch.client.NewOrderFeignClient;
import cn.van.daijia.driver.client.DriverInfoFeignClient;
import cn.van.daijia.driver.service.OrderService;
import cn.van.daijia.map.client.MapFeignClient;
import cn.van.daijia.model.entity.order.OrderInfo;
import cn.van.daijia.model.form.map.CalculateDrivingLineForm;
import cn.van.daijia.model.form.order.StartDriveForm;
import cn.van.daijia.model.form.order.UpdateOrderCartForm;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.map.DrivingLineVo;
import cn.van.daijia.model.vo.order.CurrentOrderInfoVo;
import cn.van.daijia.model.vo.order.NewOrderDataVo;
import cn.van.daijia.model.vo.order.OrderInfoVo;
import cn.van.daijia.order.client.OrderInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Autowired
    private MapFeignClient mapFeignClient;
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

    /**
     * 抢单
     * @param driverId
     * @param orderId
     * @return
     */
    @Override
    public Boolean robNewOrder(Long driverId, Long orderId) {

        return orderInfoFeignClient.robNewOrder(driverId, orderId).getData();
    }

    @Override
    public CurrentOrderInfoVo searchDriverCurrentOrder(Long driverId) {
        return orderInfoFeignClient.searchDriverCurrentOrder(driverId).getData();
    }


    /**
     * 获取订单账单详细信息
     * @param orderId
     * @param driverId
     * @return
     */
    @Override
    public OrderInfoVo getOrderInfo(Long orderId, Long driverId) {
        OrderInfo orderInfo = orderInfoFeignClient.getOrderInfo(orderId).getData();
        if(orderInfo.getDriverId()!=driverId){
            throw new GuiguException(ResultCodeEnum.ILLEGAL_REQUEST);
        }
        OrderInfoVo orderInfoVo=new OrderInfoVo();
        orderInfoVo.setOrderId(orderId);
        BeanUtils.copyProperties(orderInfo,orderInfoVo);
        return orderInfoVo;
    }

    //计算最佳驾驶路线
    @Override
    public DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm) {
        return mapFeignClient.calculateDrivingLine(calculateDrivingLineForm).getData();
    }

    /**
     * 司机到达代驾起始地点
     * @param orderId
     * @param driverId
     * @return
     */
    @Override
    public Boolean driverArriveStartLocation(Long orderId, Long driverId) {
        return orderInfoFeignClient.driverArriveStartLocation(orderId, driverId).getData();
    }

    /**
     * 更新代驾车辆信息
     * @param updateOrderCartForm
     * @return
     */
    @Override
    public Boolean updateOrderCart(UpdateOrderCartForm updateOrderCartForm) {

        return orderInfoFeignClient.updateOrderCart(updateOrderCartForm).getData();
    }

    //开始代驾服务
    @Override
    public Boolean startDrive(StartDriveForm startDriveForm) {
        return orderInfoFeignClient.startDrive(startDriveForm).getData();
    }


}
