package cn.van.daijia.driver.service;

import cn.van.daijia.model.entity.driver.DriverInfo;
import cn.van.daijia.model.vo.driver.DriverLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DriverInfoService extends IService<DriverInfo> {

    //微信小程序司机端登录
    Long login(String code);
    //获取司机登录信息
    DriverLoginVo getDriverLoginInfo(Long driverId);
}
