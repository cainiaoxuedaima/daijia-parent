package cn.van.daijia.customer.controller;

import cn.van.daijia.common.constant.RedisConstant;
import cn.van.daijia.common.execption.GuiguException;
import cn.van.daijia.common.login.VanLogin;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.result.ResultCodeEnum;
import cn.van.daijia.common.util.AuthContextHolder;
import cn.van.daijia.customer.client.CustomerInfoFeignClient;
import cn.van.daijia.customer.service.CustomerService;
import cn.van.daijia.model.form.customer.UpdateWxPhoneForm;
import cn.van.daijia.model.vo.customer.CustomerLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "客户API接口管理")
@RestController
@RequestMapping("/customer")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    //在 Java 中，@RequestHeader 注解通常用于从 HTTP 请求的头部获取特定的参数值。
    @Operation(summary="获取客户登录信息")
    @VanLogin
    @GetMapping("/getCustomerLoginInfo")
    public Result<CustomerLoginVo> getCustomerLoginInfo(){
        //1.从ThreadLocal获取用户id
        //AuthContextHolder 通常是在涉及身份验证和授权的编程环境中使用的一个组件或工具。
        Long customerId = AuthContextHolder.getUserId();
        //调用service
        CustomerLoginVo customerLoginVo =customerService.getCustomerInfo(customerId);
        return Result.ok(customerLoginVo);
    }
//    //在 Java 中，@RequestHeader 注解通常用于从 HTTP 请求的头部获取特定的参数值。
//    @Operation(summary="获取客户登录信息")
//    @GetMapping("/getCustomerLoginInfo")
//    public Result<CustomerLoginVo>
//    getCustomerLoginInfo(@RequestHeader(value="token")String token){
//        //1. 从请求头得到token字符串
//        //调用service
//        CustomerLoginVo customerLoginVo =customerService.getCustomerLoginInfo(token);
//        return Result.ok(customerLoginVo);
//    }
    @Operation(summary = "小程序授权登录")
    @GetMapping("/login/{code}")
    public Result<String>wxLogin(@PathVariable String code){
        return Result.ok(customerService.login(code));
    }


    @Operation(summary = "更新客户手机号")
    @VanLogin
    @PostMapping("/updateWxPhone")
    public Result updateWxPhone(@RequestBody UpdateWxPhoneForm updateWxPhoneForm){
        updateWxPhoneForm.setCustomerId(AuthContextHolder.getUserId());
        return Result.ok(customerService.updateWxPhoneNumber(updateWxPhoneForm));
    }
}

