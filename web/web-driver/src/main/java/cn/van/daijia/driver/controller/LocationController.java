package cn.van.daijia.driver.controller;

import cn.van.daijia.common.execption.GuiguException;
import cn.van.daijia.common.login.VanLogin;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.result.ResultCodeEnum;
import cn.van.daijia.driver.client.DriverInfoFeignClient;
import cn.van.daijia.driver.service.LocationService;
import cn.van.daijia.model.entity.driver.DriverSet;
import cn.van.daijia.model.form.map.OrderServiceLocationForm;
import cn.van.daijia.model.form.map.UpdateDriverLocationForm;
import cn.van.daijia.model.form.map.UpdateOrderLocationForm;
import cn.van.daijia.model.vo.map.OrderLocationVo;
import cn.van.daijia.order.client.OrderInfoFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "位置API接口管理")
@RestController
@RequestMapping(value="/location")
@SuppressWarnings({"unchecked", "rawtypes"})
public class LocationController {
	
    @Autowired
    private LocationService locationService;

    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;
    /**
     * 开启司机接单
     * @param updateDriverLocationForm
     * @return
     */
    @Operation(summary = "开启司机接单")
    @VanLogin
    @PostMapping("/updateDriverLocation")
    public Result<Boolean>updateDriverLocation(@RequestBody UpdateDriverLocationForm updateDriverLocationForm){
        //根据司机id获取司机个性化的设置信息
        Long driverId = updateDriverLocationForm.getDriverId();
        Result<DriverSet> driverSetResult = driverInfoFeignClient.getDriverSet(driverId);
        DriverSet driverSet = driverSetResult.getData();
        if(driverSet.getServiceStatus()==1){
            return Result.ok(locationService.updateDriverLocation(updateDriverLocationForm));
        }else{
            //没有接单
            throw new GuiguException(ResultCodeEnum.NO_START_SERVICE);
        }
        //判断：如果司机开始接单 更新司机位置信息，如果没有
    }
    @Operation(summary = "司机赶往代驾起始点：更新订单位置到Redis缓存")
    @VanLogin
    @PostMapping("/updateOrderLocationToCache")
    public Result updateOrderLocationToCache(@RequestBody UpdateOrderLocationForm updateOrderLocationForm) {
        return Result.ok(locationService.updateOrderLocationToCache(updateOrderLocationForm));
    }

    //批量保存代驾服务订单位置
    @PostMapping("/saveOrderServiceLocation")
    public Result<Boolean> saveOrderServiceLocation(@RequestBody List<OrderServiceLocationForm> orderLocationServiceFormList) {
        return Result.ok(locationService.saveOrderServiceLocation(orderLocationServiceFormList));
    }

    @Operation(summary = "司机赶往代驾起始点：获取订单经纬度位置")
    @GetMapping("/getCacheOrderLocation/{orderId}")
    public Result<OrderLocationVo> getCacheOrderLocation(@PathVariable Long orderId) {
        return Result.ok(locationService.getCacheOrderLocation(orderId));
    }



}

