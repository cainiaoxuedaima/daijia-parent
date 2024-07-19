package cn.van.daijia.driver.service;

import cn.van.daijia.model.form.driver.DriverFaceModelForm;
import cn.van.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import cn.van.daijia.model.vo.driver.DriverAuthInfoVo;

public interface DriverService {

    //小程序授权登录
    String login(String code);
    //获取司机认证信息
    DriverAuthInfoVo getDriverAuthInfo(Long driverId);
    //修改司机认证信息
    Boolean updateDriverAuthInfo(UpdateDriverAuthInfoForm updateDriverAuthInfoForm);

    //创建司机人脸模型
    Boolean creatDriverFaceModel(DriverFaceModelForm driverFaceModelForm);
}
