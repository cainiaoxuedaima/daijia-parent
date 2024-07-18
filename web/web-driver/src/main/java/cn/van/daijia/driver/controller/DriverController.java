package cn.van.daijia.driver.controller;

import cn.van.daijia.common.login.VanLogin;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.util.AuthContextHolder;
import cn.van.daijia.driver.client.DriverInfoFeignClient;
import cn.van.daijia.driver.service.DriverService;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.driver.DriverLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "司机API接口管理")
@RestController
@RequestMapping(value="/driver")
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverController {
	@Autowired
    private DriverService driverService;

    @Autowired
    private DriverInfoFeignClient  driverInfoFeignClient;

    @Operation(summary="获取司机登录信息")
    @VanLogin
    @GetMapping("/getDriverLoginInfo")
    public Result<DriverLoginVo>getDriverLoginInfo(){
        //1. 获取用户id
        Long driverId = AuthContextHolder.getUserId();
        //2. 远程调用获取司机的信息
        Result<DriverLoginVo> loginVoResult = driverInfoFeignClient.getDriverLoginInfo(driverId);
        DriverLoginVo driverLoginVo = loginVoResult.getData();
        return Result.ok(driverLoginVo);
    }
    @Operation(summary ="小程序授权登录")
    @GetMapping("/login/{code}")
    public Result<String>login(@PathVariable String code){
        return Result.ok(driverService.login(code));
    }



}

