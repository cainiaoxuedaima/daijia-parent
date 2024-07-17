package cn.van.daijia.customer.controller;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.customer.service.CustomerInfoService;
import cn.van.daijia.model.entity.customer.CustomerInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/customer/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerInfoController {

	@Autowired
	private CustomerInfoService customerInfoService;

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

