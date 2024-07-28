package cn.van.daijia.order.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.util.AuthContextHolder;
import cn.van.daijia.model.entity.order.OrderInfo;
import cn.van.daijia.model.form.order.*;
import cn.van.daijia.model.vo.order.CurrentOrderInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(value = "service-order")
public interface OrderInfoFeignClient {

    /**
     * 保存订单信息
     * @param orderInfoForm
     * @return
     */
    @PostMapping("/order/info/saveOrderInfo")
    Result<Long>saveOrderInfo(@RequestBody OrderInfoForm orderInfoForm);


    /**
     * 根据订单id获取订单状态
     * @param orderId
     * @return
     */
    @DeleteMapping("/order/info/getOrderStatus/{orderId}")
    Result<Integer>getOrderStatus(@PathVariable Long orderId);

    @GetMapping("/order/info/robNewOrder/{driverId}/{orderId}")
    Result<Boolean>robNewOrder(@PathVariable("driverId")Long driverId,@PathVariable("orderId")Long orderId);

    /**
     * 乘客端查找当前订单
     * @param customerId
     * @return
     */
    @GetMapping("/order/info/searchCustomerCurrentOrder/{customerId}")
    Result<CurrentOrderInfoVo> searchCustomerCurrentOrder(@PathVariable("customerId") Long customerId);

    /**
     * 司机端查找当前订单
     * @param driverId
     * @return
     */
    @GetMapping("/order/info/searchDriverCurrentOrder/{driverId}")
    Result<CurrentOrderInfoVo> searchDriverCurrentOrder(@PathVariable("driverId") Long driverId);

    /**
     * 根据订单id获取订单信息
     * @param orderId
     * @return
     */
    @GetMapping("/order/info/getOrderInfo/{orderId}")
    Result<OrderInfo> getOrderInfo(@PathVariable("orderId") Long orderId);

    /**
     * 司机到达起始点
     * @param orderId
     * @param driverId
     * @return
     */
    @GetMapping("/order/info/driverArriveStartLocation/{orderId}/{driverId}")
    Result<Boolean> driverArriveStartLocation(@PathVariable("orderId") Long orderId, @PathVariable("driverId") Long driverId);

    /**
     * 更新代驾车辆信息
     * @param updateOrderCartForm
     * @return
     */
    @PostMapping("/order/info/updateOrderCart")
    Result<Boolean> updateOrderCart(@RequestBody UpdateOrderCartForm updateOrderCartForm);

    @PostMapping("/order/info/startDrive")
    Result<Boolean>startDrive(@RequestBody StartDriveForm startDriveForm);

    /**
     *  根据时间段获取订单数
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/order/info/getOrderNumByTime/{startTime}/{endTime}")
    Result<Long> getOrderNumByTime(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime);


    /**
     * 结束代驾服务更新订单账单
     * @param updateOrderBillForm
     * @return
     */
    @PostMapping("/order/info/endDrive")
    Result<Boolean> endDrive(@RequestBody UpdateOrderBillForm updateOrderBillForm);



}