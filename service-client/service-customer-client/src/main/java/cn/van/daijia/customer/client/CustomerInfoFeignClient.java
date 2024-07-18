package cn.van.daijia.customer.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.entity.customer.CustomerInfo;
import cn.van.daijia.model.form.customer.UpdateWxPhoneForm;
import cn.van.daijia.model.vo.customer.CustomerLoginVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-customer")
public interface CustomerInfoFeignClient {

    @GetMapping("/customer/info/login/{code}")
    Result<Long> login(@PathVariable String code);

    @GetMapping("/customer/info/getCustomerLoginInfo/{customerId}")
    Result <CustomerLoginVo>getCustomerLoginInfo(@PathVariable("customerId") Long customerId );

    /**
     * 更新微信手机号
     * @param updateWxPhoneForm
     * @return
     */
    @PostMapping("/customer/info/updateWxPhoneNumber")
    Result<Boolean>updateWxPhoneNumber(@RequestBody UpdateWxPhoneForm updateWxPhoneForm);
}