package cn.van.daijia.driver.service;

import cn.van.daijia.model.form.map.CalculateDrivingLineForm;
import cn.van.daijia.model.form.order.OrderFeeForm;
import cn.van.daijia.model.form.order.StartDriveForm;
import cn.van.daijia.model.form.order.UpdateOrderCartForm;
import cn.van.daijia.model.vo.base.PageVo;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.map.DrivingLineVo;
import cn.van.daijia.model.vo.order.CurrentOrderInfoVo;
import cn.van.daijia.model.vo.order.NewOrderDataVo;
import cn.van.daijia.model.vo.order.OrderInfoVo;

import java.util.List;

public interface OrderService {


    // 获取订单状态
    Integer getOrderStatus(Long orderId);

    // 添加并开始新订单任务调度
    List<NewOrderDataVo> findNewOrderQueueData(Long driverId);

    // 司机抢单
    Boolean robNewOrder(Long driverId, Long orderId);

    //司机端查找当前订单
    CurrentOrderInfoVo searchDriverCurrentOrder(Long driverId);

    //获取订单账单详细信息
    OrderInfoVo getOrderInfo(Long orderId, Long driverId);

    //计算最佳驾驶路线
    DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm);


    //司机到达代驾起始地点
    Boolean driverArriveStartLocation(Long orderId, Long driverId);

    //更新代驾车辆信息
    Boolean updateOrderCart(UpdateOrderCartForm updateOrderCartForm);

    //开始代驾服务
    Boolean startDrive(StartDriveForm startDriveForm);

    //结束代驾服务更新订单账单
    Boolean endDrive(OrderFeeForm orderFeeForm);

    //获取司机订单分页列表
    PageVo findDriverOrderPage(Long driverId, Long page, Long limit);
}
