package cn.van.daijia.order.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.form.order.OrderInfoForm;
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
}