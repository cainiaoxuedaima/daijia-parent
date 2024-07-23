package cn.van.daijia.map.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.form.map.SearchNearByDriverForm;
import cn.van.daijia.model.form.map.UpdateDriverLocationForm;
import cn.van.daijia.model.vo.map.NearByDriverVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "service-map")
public interface LocationFeignClient {
    /**
     * 开启接单服务：更新司机经纬度位置
     * @param updateDriverLocationForm
     * @return
     */
    @PostMapping("/map/location/updateDriverLocation")
    Result<Boolean> updateDriverLocation(@RequestBody UpdateDriverLocationForm updateDriverLocationForm);

    /**
     * 关闭接单服务：删除司机经纬度位置
     * @param driverId
     * @return
     */
    @DeleteMapping("/map/location/removeDriverLocation/{driverId}")
    Result<Boolean> removeDriverLocation(@PathVariable("driverId") Long driverId);


    @PostMapping("/map/location/searchNearByDriver")
    Result<List<NearByDriverVo>>searchNearByDriver(@RequestBody
                                                              SearchNearByDriverForm searchNearByDriverForm);
}