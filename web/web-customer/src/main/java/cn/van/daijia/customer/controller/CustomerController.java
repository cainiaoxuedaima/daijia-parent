package cn.van.daijia.customer.controller;

import cn.van.daijia.common.constant.RedisConstant;
import cn.van.daijia.common.execption.GuiguException;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.result.ResultCodeEnum;
import cn.van.daijia.customer.client.CustomerInfoFeignClient;
import cn.van.daijia.customer.service.CustomerService;
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
    @GetMapping("/getCustomerLoginInfo")
    public Result<CustomerLoginVo>
    getCustomerLoginInfo(@RequestHeader(value="token")String token){
        //1. 从请求头得到token字符串
        //调用service
        CustomerLoginVo customerLoginVo =customerService.getCustomerLoginInfo(token);
        return Result.ok(customerLoginVo);
    }
    @Operation(summary = "小程序授权登录")
    @GetMapping("/login/{code}")
    public Result<String>wxLogin(@PathVariable String code){
        return Result.ok(customerService.login(code));
    }
}

