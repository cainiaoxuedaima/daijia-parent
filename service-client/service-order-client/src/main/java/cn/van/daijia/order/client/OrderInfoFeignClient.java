package cn.van.daijia.order.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.entity.order.OrderInfo;
import cn.van.daijia.model.form.order.OrderInfoForm;
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
}