package cn.van.daijia.driver.controller;

import cn.van.daijia.common.login.VanLogin;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.util.AuthContextHolder;
import cn.van.daijia.driver.client.DriverInfoFeignClient;
import cn.van.daijia.driver.service.DriverService;
import cn.van.daijia.model.form.driver.DriverFaceModelForm;
import cn.van.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import cn.van.daijia.model.vo.driver.DriverAuthInfoVo;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.driver.DriverLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @Operation(summary = "获取司机认证信息")
    @VanLogin
    @GetMapping("/getDriverAuthInfo")
    public Result<DriverAuthInfoVo>getDriverAuthInfo(){
        Long driverId = AuthContextHolder.getUserId();
        return Result.ok(driverService.getDriverAuthInfo(driverId));
    }

    @Operation(summary = "司机修改认证信息")
    @VanLogin
    @PostMapping("/updateDriverAuthInfo")
    public Result<Boolean>updateDriverAuthInfo(@RequestBody UpdateDriverAuthInfoForm updateDriverAuthInfoForm){
        updateDriverAuthInfoForm.setDriverId(AuthContextHolder.getUserId());
        return Result.ok(driverService.updateDriverAuthInfo(updateDriverAuthInfoForm));
    }
    @Operation(summary = "创建司机人脸模型")
    @VanLogin
    @PostMapping("/creatDriverFaceModel")
    public Result<Boolean> creatDriverFaceModel(@RequestBody DriverFaceModelForm driverFaceModelForm) {
        driverFaceModelForm.setDriverId(AuthContextHolder.getUserId());
        return Result.ok(driverService.creatDriverFaceModel(driverFaceModelForm));
    }


    @Operation(summary = "判断司机当前是否进行过人脸识别")
    @VanLogin
    @GetMapping("/isFaceRecognition")
    Result<Boolean>isFaceRecognition(){
        return Result.ok(driverService.isFaceRecognition(AuthContextHolder.getUserId()));
    }

    @Operation(summary = "验证司机人脸")
    @VanLogin
    @PostMapping("/verifyDriverFace")
    public Result<Boolean>verifyDriverFace(@RequestBody DriverFaceModelForm driverFaceModelForm) {
        driverFaceModelForm.setDriverId(AuthContextHolder.getUserId());
        return Result.ok(driverService.verifyDriverFace(driverFaceModelForm));
    }


    @Operation(summary = "开始接单服务")
    @VanLogin
    @GetMapping("/startService")
    public Result<Boolean> startService() {
        Long driverId = AuthContextHolder.getUserId();
        return Result.ok(driverService.startService(driverId));
    }

    @Operation(summary = "停止接单服务")
    @VanLogin
    @GetMapping("/stopService")
    public Result<Boolean> stopService() {
        Long driverId = AuthContextHolder.getUserId();
        return Result.ok(driverService.stopService(driverId));
    }
}

