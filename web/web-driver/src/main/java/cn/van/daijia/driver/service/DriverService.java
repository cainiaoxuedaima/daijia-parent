package cn.van.daijia.driver.service;

import cn.van.daijia.model.vo.driver.DriverAuthInfoVo;

public interface DriverService {

    //小程序授权登录
    String login(String code);
    //获取司机认证信息
    DriverAuthInfoVo getDriverAuthInfo(Long driverId);
}
