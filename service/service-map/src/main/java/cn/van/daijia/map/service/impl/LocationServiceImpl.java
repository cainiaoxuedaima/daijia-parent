package cn.van.daijia.map.service.impl;

import cn.van.daijia.common.constant.RedisConstant;
import cn.van.daijia.common.constant.SystemConstant;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.driver.client.DriverInfoFeignClient;
import cn.van.daijia.map.service.LocationService;
import cn.van.daijia.model.entity.driver.DriverSet;
import cn.van.daijia.model.form.map.SearchNearByDriverForm;
import cn.van.daijia.model.form.map.UpdateDriverLocationForm;
import cn.van.daijia.model.vo.map.NearByDriverVo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class LocationServiceImpl implements LocationService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;
    /**
     * 更新司机经纬度
     * @param updateDriverLocationForm
     * @return
     */
    @Override
    public Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm) {
        //把司机位置信息添加到redis里面geo
        Point point=new Point(updateDriverLocationForm.getLongitude().doubleValue(),
                                updateDriverLocationForm.getLatitude().doubleValue());
        //添加到redis
        redisTemplate.opsForGeo().add(
                RedisConstant.DRIVER_GEO_LOCATION,
                point,
                updateDriverLocationForm.getDriverId().toString());

        return null;
    }

    /**
     * 移除司机经纬度
     * @param driverId
     * @return
     */
    @Override
    public Boolean removeDriverLocation(Long driverId) {
        redisTemplate.opsForGeo().remove(RedisConstant.DRIVER_GEO_LOCATION,driverId.toString());
        return true;
    }

    /**
     * 搜索附近满足条件的司机
     * @param searchNearByDriverForm
     * @return
     */
    @Override
    public List<NearByDriverVo> searchNearByDriver(SearchNearByDriverForm searchNearByDriverForm) {
        //搜索经纬度5公里以内的司机
        //操作redis里面的geo
        //创建circle对象,point,distance;
        Point point=new Point(searchNearByDriverForm.getLongitude().doubleValue(),
                searchNearByDriverForm.getLatitude().doubleValue());
        Distance distance=new Distance(SystemConstant.NEARBY_DRIVER_RADIUS,
                                        RedisGeoCommands.DistanceUnit.KILOMETERS);
        Circle circle=new Circle(point,distance);
        //定义GEO参数
        RedisGeoCommands.GeoCommandArgs redisGeo=RedisGeoCommands.
                GeoRadiusCommandArgs.
                newGeoRadiusArgs().
                includeDistance().
                sortAscending().
                includeCoordinates();
        GeoResults<RedisGeoCommands.GeoLocation<String>>result=
                redisTemplate.opsForGeo().radius(RedisConstant.DRIVER_GEO_LOCATION,circle, (RedisGeoCommands.GeoRadiusCommandArgs) redisGeo);

        //查询redis最终返回List集合
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = result.getContent();

        //查询List集合进行处理
        //遍历list集合，得到每个司机信息
        //根据每个司机个性化设置信息判断
        List<NearByDriverVo>list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(content)){
            Iterator<GeoResult<RedisGeoCommands.GeoLocation<String>>> iterator = content.iterator();
            while (iterator.hasNext()){
                GeoResult<RedisGeoCommands.GeoLocation<String>> item = iterator.next();

                //获取司机id
                Long driverId = Long.parseLong(item.getContent().getName());

                //远程调用,根据司机id个性化设置信息
                Result<DriverSet> driverSetResult = driverInfoFeignClient.getDriverSet(driverId);
                DriverSet driverSet = driverSetResult.getData();
                
                //判断订单里程order_distance
                BigDecimal orderDistance = driverSet.getOrderDistance();
                //orderDistance=0 司机没有限制的
                //如果不等于0，比如30 接单30公里代驾订单
                //接单距离-当前单子的距离<0不符合条件

                if(orderDistance.doubleValue()!=0&&
                        orderDistance.subtract(searchNearByDriverForm.getMileageDistance()).doubleValue()<0){
                    continue;
                }
                //判断接单里程accept_distance
                BigDecimal currentDistance=
                        new BigDecimal(item.getDistance().getValue()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal acceptDistance = driverSet.getAcceptDistance();
                if(acceptDistance.doubleValue()!=0 &&
                    acceptDistance.subtract(currentDistance).doubleValue()<0){
                    continue;
                }
                NearByDriverVo nearByDriverVo = new NearByDriverVo();
                nearByDriverVo.setDriverId(driverId);
                nearByDriverVo.setDistance(currentDistance);
                list.add(nearByDriverVo);
            }
        }
        return list;
    }
}
