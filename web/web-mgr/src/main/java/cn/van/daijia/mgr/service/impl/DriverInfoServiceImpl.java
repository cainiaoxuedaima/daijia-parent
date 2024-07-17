package cn.van.daijia.mgr.service.impl;

import cn.van.daijia.mgr.service.DriverInfoService;
import cn.van.daijia.driver.client.DriverInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverInfoServiceImpl implements DriverInfoService {

    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;



}