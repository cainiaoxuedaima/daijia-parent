package cn.van.daijia.driver.service.impl;

import cn.van.daijia.common.constant.RedisConstant;
import cn.van.daijia.common.execption.GuiguException;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.result.ResultCodeEnum;
import cn.van.daijia.driver.client.DriverInfoFeignClient;
import cn.van.daijia.driver.service.LocationService;
import cn.van.daijia.map.client.LocationFeignClient;
import cn.van.daijia.map.repository.OrderServiceLocationRepository;
import cn.van.daijia.model.entity.driver.DriverSet;
import cn.van.daijia.model.entity.map.OrderServiceLocation;
import cn.van.daijia.model.form.map.OrderServiceLocationForm;
import cn.van.daijia.model.form.map.UpdateDriverLocationForm;
import cn.van.daijia.model.form.map.UpdateOrderLocationForm;
import cn.van.daijia.model.vo.map.OrderLocationVo;
import cn.van.daijia.order.client.OrderInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationFeignClient locationFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;

    @Autowired
    private OrderServiceLocationRepository orderServiceLocationRepository;
    //更新司机位置
    @Override
    public Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm) {
        //根据司机id获取司机个性化设置信息
        Long driverId = updateDriverLocationForm.getDriverId();
        Result<DriverSet> driverSetResult = driverInfoFeignClient.getDriverSet(driverId);
        DriverSet driverSet = driverSetResult.getData();

        //判断：如果司机开始接单，更新位置信息
        if(driverSet.getServiceStatus() == 1) {
            Result<Boolean> booleanResult = locationFeignClient.updateDriverLocation(updateDriverLocationForm);
            return booleanResult.getData();
        } else {
            //没有接单
            throw new GuiguException(ResultCodeEnum.NO_START_SERVICE);
        }
    }

    /**
     * 司机赶往代驾起始点：更新订单地址到缓存
     * @param updateOrderLocationForm
     * @return
     */
    @Override
    public Boolean updateOrderLocationToCache(UpdateOrderLocationForm updateOrderLocationForm) {
        return locationFeignClient.updateOrderLocationToCache(updateOrderLocationForm).getData();
    }

    @Override
    public OrderLocationVo getCacheOrderLocation(Long orderId) {
        String key = RedisConstant.UPDATE_ORDER_LOCATION + orderId;
        OrderLocationVo orderLocationVo = (OrderLocationVo)redisTemplate.opsForValue().get(key);
        return orderLocationVo;
    }

    //批量保存代驾服务订单位置
    @Override
    public Boolean saveOrderServiceLocation(List<OrderServiceLocationForm> orderLocationServiceFormList) {

        List<OrderServiceLocation>list=new ArrayList<>();
        orderLocationServiceFormList.forEach(orderServiceLocationForm -> {
            OrderServiceLocation orderServiceLocation=new OrderServiceLocation();
            BeanUtils.copyProperties(orderServiceLocationForm,orderServiceLocation);
            orderServiceLocation.setId(ObjectId.get().toString());
            orderServiceLocation.setCreateTime(new Date());

            list.add(orderServiceLocation);
        });
        //批量添加到MongoDB
        orderServiceLocationRepository.saveAll(list);


        return true;
    }
}
