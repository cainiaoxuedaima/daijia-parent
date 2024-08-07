package cn.van.daijia.driver.service;

import cn.van.daijia.model.entity.driver.DriverInfo;
import cn.van.daijia.model.entity.driver.DriverSet;
import cn.van.daijia.model.form.driver.DriverFaceModelForm;
import cn.van.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import cn.van.daijia.model.vo.driver.DriverAuthInfoVo;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.driver.DriverLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DriverInfoService extends IService<DriverInfo> {

    //微信小程序司机端登录
    Long login(String code);
    //获取司机登录信息
    DriverLoginVo getDriverLoginInfo(Long driverId);

    //获取司机认证信息
    DriverAuthInfoVo getDriverAuthInfo(Long driverId);

    //更新司机认证信息
    Boolean updateDriverAuthInfo(UpdateDriverAuthInfoForm updateDriverAuthInfoForm);

    //创建司机人脸模型
    Boolean creatDriverFaceModel(DriverFaceModelForm driverFaceModelForm);

    //获取司机设置信息
    DriverSet getDriverSet(Long driverId);

    //判断司机当前是否进行过人脸识别
    Boolean isFaceRecognition(Long driverId);

    //验证司机人脸
    Boolean verifyDriverFace(DriverFaceModelForm driverFaceModelForm);

    ////更新司机接单状态
    Boolean updateServiceStatus(Long driverId, Integer status);

    //获取司机基本信息
    DriverInfoVo getDriverInfoOrder(Long driverId);
}
