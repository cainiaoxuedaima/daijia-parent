package cn.van.daijia.map.service;

import cn.van.daijia.model.form.map.SearchNearByDriverForm;
import cn.van.daijia.model.form.map.UpdateDriverLocationForm;
import cn.van.daijia.model.vo.map.NearByDriverVo;

import java.util.List;

public interface LocationService {

    // 更新司机经纬度
    Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm);

    // 移除司机经纬度
    Boolean removeDriverLocation(Long driverId);
    // 查询附近司机
    List<NearByDriverVo> searchNearByDriver(SearchNearByDriverForm searchNearByDriverForm);
}
