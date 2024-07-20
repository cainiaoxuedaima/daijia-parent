package cn.van.daijia.map.service;

import cn.van.daijia.model.form.map.CalculateDrivingLineForm;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.map.DrivingLineVo;

public interface MapService {

    // 计算驾驶路线
    DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm);
}
