package cn.van.daijia.map.controller;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.map.service.MapService;
import cn.van.daijia.model.form.map.CalculateDrivingLineForm;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.map.DrivingLineVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "地图API接口管理")
@RestController
@RequestMapping("/map")
@SuppressWarnings({"unchecked", "rawtypes"})
public class MapController {

    @Autowired
    private MapService mapService;

    @Operation(summary ="计算驾驶路线")
    @PostMapping("/calculateDrivingLine")
    public Result<DrivingLineVo>calculateDrivingLine(@RequestBody CalculateDrivingLineForm calculateDrivingLineForm){

        return Result.ok(mapService.calculateDrivingLine(calculateDrivingLineForm));

    }

}

