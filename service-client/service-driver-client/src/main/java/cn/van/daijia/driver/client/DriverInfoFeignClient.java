package cn.van.daijia.driver.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.driver.DriverLoginVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-driver")
public interface DriverInfoFeignClient {
    /**
     * 小程序授权登录
     * @param code
     * @return
     */
    @GetMapping("/device/info/login/{code}")
    Result<Long> login(@PathVariable("code") String code);


    @GetMapping("/device/info/getDriverLoginInfo/{driverId}")
    Result<DriverLoginVo>getDriverLoginInfo(@PathVariable("driverId") Long driverId);

}