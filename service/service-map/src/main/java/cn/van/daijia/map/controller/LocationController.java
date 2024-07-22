package cn.van.daijia.map.controller;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.map.service.LocationService;
import cn.van.daijia.model.form.map.SearchNearByDriverForm;
import cn.van.daijia.model.form.map.UpdateDriverLocationForm;
import cn.van.daijia.model.vo.map.NearByDriverVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "位置API接口管理")
@RestController
@RequestMapping("/map/location")
@SuppressWarnings({"unchecked", "rawtypes"})
public class LocationController {

    @Autowired
    private LocationService locationService;

    //司机开启接单，更新司机位置信息
    @Operation(summary = "开启接单服务：更新司机经纬度")
    @PostMapping("/updateDriverLocation")
    public Result<Boolean> updateDriverLocation(@RequestBody UpdateDriverLocationForm updateDriverLocationForm)
    {
        return Result.ok(locationService.updateDriverLocation(updateDriverLocationForm));
    }

    //司机关闭接单，删除司机位置信息
    @Operation(summary = "开启接单服务：更新司机经纬度")
    @PostMapping("/removeDriverLocation/{driverId}")
    public Result<Boolean> removeDriverLocation(@PathVariable Long driverId)
    {
        return Result.ok(locationService.removeDriverLocation(driverId));
    }

    /**
     * 搜索附近满足条件的司机
     * @param searchNearByDriverForm
     * @return
     */
    @Operation(summary = "搜索附近满足条件的司机")
    @PostMapping("/searchNearByDriver")
    public Result<List<NearByDriverVo>>searchNearByDriver(@RequestBody SearchNearByDriverForm searchNearByDriverForm){
        return Result.ok(locationService.searchNearByDriver(searchNearByDriverForm));
    }
}

