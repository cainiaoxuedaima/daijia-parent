package cn.van.daijia.driver.service.impl;

import cn.van.daijia.common.constant.RedisConstant;
import cn.van.daijia.common.execption.GuiguException;
import cn.van.daijia.common.login.VanLogin;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.result.ResultCodeEnum;
import cn.van.daijia.common.util.AuthContextHolder;
import cn.van.daijia.dispatch.client.NewOrderFeignClient;
import cn.van.daijia.driver.client.DriverInfoFeignClient;
import cn.van.daijia.driver.service.DriverService;
import cn.van.daijia.map.client.LocationFeignClient;
import cn.van.daijia.model.form.driver.DriverFaceModelForm;
import cn.van.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import cn.van.daijia.model.vo.driver.DriverAuthInfoVo;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.driver.DriverLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private LocationFeignClient locationFeignClient;

    @Autowired
    private NewOrderFeignClient newOrderFeignClient;
    /**
     * 登录过程
     * @param code
     * @return
     */
    @Override
    public String login(String code) {
        //远程调用，得到司机id
        Result<Long> longResult = driverInfoFeignClient.login(code);

        //判断状态码 和id不为空
        Integer curResult = longResult.getCode();
        if(curResult!=200){
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        Long driverId = longResult.getData();
        if(driverId==null){
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        //生成token字符串
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().
                set(RedisConstant.USER_LOGIN_KEY_PREFIX+token,
                        driverId.toString(),
                        RedisConstant.USER_LOGIN_KEY_TIMEOUT,
                        TimeUnit.MINUTES);

        //返回token
        return token;
    }

    @Override
    public DriverAuthInfoVo getDriverAuthInfo(Long driverId) {
        return driverInfoFeignClient.getDriverAuthInfo(driverId).getData();
    }

    @Override
    public Boolean updateDriverAuthInfo(UpdateDriverAuthInfoForm updateDriverAuthInfoForm) {
        Result<Boolean> booleanResult = driverInfoFeignClient.updateDriverAuthInfo(updateDriverAuthInfoForm);
        Boolean data = booleanResult.getData();
        return data;
    }

    //创建司机人脸模型
    @Override
    public Boolean creatDriverFaceModel(DriverFaceModelForm driverFaceModelForm) {
        Result<Boolean> booleanResult = driverInfoFeignClient.creatDriverFaceModel(driverFaceModelForm);
        return booleanResult.getData();
    }

    // 判断司机当前是否进行过人脸识别
    @Override
    public Boolean isFaceRecognition(Long userId) {
        return driverInfoFeignClient.isFaceRecognition(userId).getData();
    }

    @Override
    public Boolean verifyDriverFace(DriverFaceModelForm driverFaceModelForm) {
        return driverInfoFeignClient.verifyDriverFace(driverFaceModelForm).getData();
    }

    /**
     * 开始接单服务
     * @param driverId
     * @return
     */
    @Override
    public Boolean startService(Long driverId) {
        //1 判断完成认证
        DriverLoginVo driverLoginVo = driverInfoFeignClient.getDriverLoginInfo(driverId).getData();
        if(driverLoginVo.getAuthStatus()!=2){
            throw new GuiguException((ResultCodeEnum.AUTH_ERROR));
        }
        //2 判断当日是否人脸识别
        Boolean isFace=driverInfoFeignClient.isFaceRecognition(driverId).getData();
        if(!isFace){
            throw new GuiguException(ResultCodeEnum.FACE_ERROR);
        }
        //3更新订单状态1 开始接单
        driverInfoFeignClient.updateServiceStatus(driverId,1);

        //4 删除redis司机位置信息
        locationFeignClient.removeDriverLocation(driverId);

        //5 清空司机临时队列数据
        newOrderFeignClient.clearNewOrderQueueData(driverId);
        return true;
    }

    /**
     * 停止接单服务
     * @param driverId
     * @return
     */
    @Override
    public Boolean stopService(Long driverId) {
        //更新司机的接单状态 0
        driverInfoFeignClient.updateServiceStatus(driverId,0);
        //删除司机位置信息
        locationFeignClient.removeDriverLocation(driverId);
        //清空司机临时队列
        newOrderFeignClient.clearNewOrderQueueData(driverId);
        return true;
    }


}
