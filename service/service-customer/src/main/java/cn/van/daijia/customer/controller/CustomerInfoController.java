package cn.van.daijia.customer.controller;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.customer.service.CustomerInfoService;
import cn.van.daijia.model.entity.customer.CustomerInfo;
import cn.van.daijia.model.vo.customer.CustomerLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customer/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerInfoController {

	@Autowired
	private CustomerInfoService customerInfoService;



	@Operation(summary = "获取客户登录信息")
	@GetMapping("/getCustomerLoginInfo/{customerId}")
	public Result<CustomerLoginVo>getCustomerLoginInfo(@PathVariable Long customerId){
		CustomerLoginVo customerLoginVo =customerInfoService.getCustomerInfo(customerId);
		return Result.ok(customerLoginVo);
	}
	//微信小程序登录接口
	@Operation(summary="微信小程序授权登录")
	@GetMapping("/login/{code}")
	public Result<Long> login(@PathVariable String code){
		return Result.ok(customerInfoService.login(code));
	}


	@Operation(summary = "获取客户基本信息")
	@GetMapping("/getCustomerInfo/{customerId}")
	public Result<CustomerInfo> getCustomerInfo(@PathVariable Long customerId) {
		return Result.ok(customerInfoService.getById(customerId));
	}
}

