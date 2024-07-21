package cn.van.daijia.order.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.form.order.OrderInfoForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "service-order")
public interface OrderInfoFeignClient {

    @PostMapping("/order/info/saveOrderInfo")
    Result<Long>saveOrderInfo(@RequestBody OrderInfoForm orderInfoForm);
}