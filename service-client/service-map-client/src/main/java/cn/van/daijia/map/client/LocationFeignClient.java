package cn.van.daijia.map.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.form.map.SearchNearByDriverForm;
import cn.van.daijia.model.form.map.UpdateDriverLocationForm;
import cn.van.daijia.model.form.map.UpdateOrderLocationForm;
import cn.van.daijia.model.vo.map.NearByDriverVo;
import cn.van.daijia.model.vo.map.OrderLocationVo;
import cn.van.daijia.model.vo.map.OrderServiceLastLocationVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    /**
     * 司机赶往代驾起始点：更新订单地址到缓存
     * @param updateOrderLocationForm
     * @return
     */
    @PostMapping("/map/location/updateOrderLocationToCache")
    Result<Boolean> updateOrderLocationToCache(@RequestBody UpdateOrderLocationForm updateOrderLocationForm);

    /**
     * 司机赶往代驾起始点：获取订单经纬度位置
     * @param orderId
     * @return
     */
    @GetMapping("/map/location/getCacheOrderLocation/{orderId}")
    Result<OrderLocationVo> getCacheOrderLocation(@PathVariable("orderId") Long orderId);

    /**
     * 代驾服务：获取订单服务最后一个位置信息
     * @param orderId
     * @return
     */
    @GetMapping("/map/location/getOrderServiceLastLocation/{orderId}")
    Result<OrderServiceLastLocationVo> getOrderServiceLastLocation(@PathVariable Long orderId);

    /**
     * 代驾服务：计算订单实际里程
     * @param orderId
     * @return
     */
    @GetMapping("/map/location/calculateOrderRealDistance/{orderId}")
    Result<BigDecimal> calculateOrderRealDistance(@PathVariable Long orderId);

}