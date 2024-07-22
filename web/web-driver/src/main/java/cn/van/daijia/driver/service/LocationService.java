package cn.van.daijia.driver.service;

import cn.van.daijia.model.form.map.UpdateDriverLocationForm;

public interface LocationService {


    // 开启接单
    Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm);
}
