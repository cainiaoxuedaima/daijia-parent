package cn.van.daijia.driver.service.impl;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.driver.service.LocationService;
import cn.van.daijia.map.client.LocationFeignClient;
import cn.van.daijia.model.form.map.UpdateDriverLocationForm;
import cn.van.daijia.order.client.OrderInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationFeignClient locationFeignClient;

    /**
     * 开启司机接单
     * @param updateDriverLocationForm
     * @return
     */
    @Override
    public Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm) {
        Result<Boolean> updateDriverLocation =
                locationFeignClient.updateDriverLocation(updateDriverLocationForm);


        return updateDriverLocation.getData();
    }
}
