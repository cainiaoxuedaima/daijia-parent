package cn.van.daijia.customer.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.entity.customer.CustomerInfo;
import cn.van.daijia.model.vo.customer.CustomerLoginVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-customer")
public interface CustomerInfoFeignClient {

    @GetMapping("/customer/info/login/{code}")
    Result<Long> login(@PathVariable String code);

    @GetMapping("/customer/info/getCustomerLoginInfo/{customerId}")
    Result <CustomerLoginVo>getCustomerLoginInfo(@PathVariable("customerId") Long customerId );
}