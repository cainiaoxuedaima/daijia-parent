package cn.van.daijia.driver.controller;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.driver.service.DriverInfoService;
import cn.van.daijia.model.entity.driver.DriverSet;
import cn.van.daijia.model.form.driver.DriverFaceModelForm;
import cn.van.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import cn.van.daijia.model.vo.driver.DriverAuthInfoVo;
import cn.van.daijia.model.vo.driver.DriverLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "司机API接口管理")
@RestController
@RequestMapping(value="/driver/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverInfoController {

    @Autowired
    private DriverInfoService driverInfoService;

    @Operation(summary = "小程序授权登录")
    @GetMapping("/login/{code}")
    private Result<Long>login(@PathVariable String code){
        return Result.ok(driverInfoService.login(code));
    }

    @Operation(summary = "获取司机的登录信息")
    @GetMapping("/getDriverLoginInfo/{driverId}")
    public Result<DriverLoginVo>getDriverLoginInfo(@PathVariable Long driverId){

        DriverLoginVo driverLoginVo = driverInfoService.getDriverLoginInfo(driverId);
        return Result.ok(driverLoginVo);
    }

    /**
     * 获取司机认证信息
     * @param driverId
     * @return
     */
    @Operation(summary = "获取司机认证信息")
    @GetMapping("/getDriverAuthInfo/{driverId}")
    public Result<DriverAuthInfoVo>getDriverAuthInfo(@PathVariable Long driverId){
        DriverAuthInfoVo driverAuthInfoVo=driverInfoService.getDriverAuthInfo(driverId);
        return Result.ok(driverAuthInfoVo);
    }

    //更新司机认证信息
    @Operation(summary ="司机修改认证信息")
    @PostMapping("/updateDriverAuthInfo")
    public Result<Boolean> updateDriverAuthInfo(@RequestBody UpdateDriverAuthInfoForm updateDriverAuthInfoForm){
        return Result.ok(driverInfoService.updateDriverAuthInfo(updateDriverAuthInfoForm));
    }

    //创建司机人脸模型
    @Operation(summary = "创建司机人脸模型")
    @PostMapping("/creatDriverFaceModel")
    public Result<Boolean> creatDriverFaceModel(@RequestBody DriverFaceModelForm driverFaceModelForm) {
        Boolean isSuccess = driverInfoService.creatDriverFaceModel(driverFaceModelForm);
        return Result.ok(isSuccess);
    }

    @Operation(summary = "获取司机设置信息")
    @GetMapping("/getDriverSet/{driverId}")
    public Result<DriverSet> getDriverSet(@PathVariable Long driverId){
        return Result.ok(driverInfoService.getDriverSet(driverId));
    }



    @Operation(summary = "判断司机当前是否进行过人脸识别")
    @GetMapping("/isFaceRecognition/{driverId}")
    Result<Boolean> isFaceRecognition(@PathVariable("driverId")Long driverId){
        return Result.ok(driverInfoService.isFaceRecognition(driverId));
    }

    /**
     * 验证司机人脸
     * @param driverFaceModelForm
     * @return
     */
    @Operation(summary = "验证司机人脸")
    @PostMapping("/verifyDriverFace")
    public Result<Boolean>verifyDriverFace(@RequestBody DriverFaceModelForm driverFaceModelForm){
        return Result.ok(driverInfoService.verifyDriverFace(driverFaceModelForm));
    }

    @Operation(summary = "更新接单状态")
    @GetMapping("/updateServiceStatus/{driverId}/{status}")
    public Result<Boolean> updateServiceStatus(@PathVariable Long driverId, @PathVariable Integer status) {
        return Result.ok(driverInfoService.updateServiceStatus(driverId, status));
    }

}

