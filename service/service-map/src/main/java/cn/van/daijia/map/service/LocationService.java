package cn.van.daijia.map.service;

import cn.van.daijia.model.form.map.SearchNearByDriverForm;
import cn.van.daijia.model.form.map.UpdateDriverLocationForm;
import cn.van.daijia.model.form.map.UpdateOrderLocationForm;
import cn.van.daijia.model.vo.map.NearByDriverVo;
import cn.van.daijia.model.vo.map.OrderLocationVo;
import cn.van.daijia.model.vo.map.OrderServiceLastLocationVo;

import java.util.List;

public interface LocationService {

    // 更新司机经纬度
    Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm);

    // 移除司机经纬度
    Boolean removeDriverLocation(Long driverId);
    // 查询附近司机
    List<NearByDriverVo> searchNearByDriver(SearchNearByDriverForm searchNearByDriverForm);

    // 更新订单地址到缓存
    Boolean updateOrderLocationToCache(UpdateOrderLocationForm updateOrderLocationForm);

    //司机赶往代驾起始点：获取订单经纬度位置
    OrderLocationVo getCacheOrderLocation(Long orderId);

    //代驾服务：获取订单服务最后一个位置信息
    OrderServiceLastLocationVo getOrderServiceLastLocation(Long orderId);
}
