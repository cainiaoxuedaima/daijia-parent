package cn.van.daijia.map.service.impl;

import cn.van.daijia.common.constant.RedisConstant;
import cn.van.daijia.common.constant.SystemConstant;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.util.LocationUtil;
import cn.van.daijia.driver.client.DriverInfoFeignClient;
import cn.van.daijia.map.repository.OrderServiceLocationRepository;
import cn.van.daijia.map.service.LocationService;
import cn.van.daijia.model.entity.driver.DriverSet;
import cn.van.daijia.model.entity.map.OrderServiceLocation;
import cn.van.daijia.model.form.map.SearchNearByDriverForm;
import cn.van.daijia.model.form.map.UpdateDriverLocationForm;
import cn.van.daijia.model.form.map.UpdateOrderLocationForm;
import cn.van.daijia.model.vo.map.NearByDriverVo;
import cn.van.daijia.model.vo.map.OrderLocationVo;
import cn.van.daijia.model.vo.map.OrderServiceLastLocationVo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OrderServiceLocationRepository orderServiceLocationRepository;

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
    //搜索附近满足条件的司机
    @Override
    public List<NearByDriverVo> searchNearByDriver(SearchNearByDriverForm searchNearByDriverForm) {
        //搜索经纬度位置5公里以内的司机
        //1 操作redis里面geo
        //创建point，经纬度位置
        Point point = new Point(searchNearByDriverForm.getLongitude().doubleValue(),
                searchNearByDriverForm.getLatitude().doubleValue());

        //定义距离，5公里
        Distance distance = new Distance(SystemConstant.NEARBY_DRIVER_RADIUS,
                RedisGeoCommands.DistanceUnit.KILOMETERS);

        //创建circle对象，point  distance
        Circle circle = new Circle(point,distance);

        //定义GEO参数，设置返回结果包含内容
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                        .includeDistance()  //包含距离
                        .includeCoordinates() //包含坐标
                        .sortAscending(); //升序

        GeoResults<RedisGeoCommands.GeoLocation<String>> result =
                redisTemplate.opsForGeo().radius(RedisConstant.DRIVER_GEO_LOCATION, circle, args);

        //2 查询redis最终返回list集合
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = result.getContent();

        //3 对查询list集合进行处理
        // 遍历list集合，得到每个司机信息
        // 根据每个司机个性化设置信息判断
        List<NearByDriverVo> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(content)) {
            Iterator<GeoResult<RedisGeoCommands.GeoLocation<String>>> iterator = content.iterator();
            while(iterator.hasNext()) {
                GeoResult<RedisGeoCommands.GeoLocation<String>> item = iterator.next();

                //获取司机id
                Long driverId = Long.parseLong(item.getContent().getName());

                //远程调用，根据司机id个性化设置信息
                Result<DriverSet> driverSetResult = driverInfoFeignClient.getDriverSet(driverId);
                DriverSet driverSet = driverSetResult.getData();
                //加一个非空判断
                if(driverSet == null) {
                    continue;
                }
                //判断订单里程order_distance
                BigDecimal orderDistance = driverSet.getOrderDistance();
                //orderDistance==0，司机没有限制的
                //如果不等于0 ，比如30，接单30公里代驾订单。
                //接单距离 - 当前单子距离  < 0,不复合条件
                // 30          35
                if(orderDistance.doubleValue() != 0
                        && orderDistance.subtract(searchNearByDriverForm.getMileageDistance()).doubleValue()<0) {
                    continue;
                }

                //判断接单里程 accept_distance
                //当前接单距离
                BigDecimal currentDistance =
                        new BigDecimal(item.getDistance().getValue()).setScale(2, RoundingMode.HALF_UP);

                BigDecimal acceptDistance = driverSet.getAcceptDistance();
                if(acceptDistance.doubleValue() !=0
                        && acceptDistance.subtract(currentDistance).doubleValue()<0) {
                    continue;
                }

                //封装复合条件数据
                NearByDriverVo nearByDriverVo = new NearByDriverVo();
                nearByDriverVo.setDriverId(driverId);
                nearByDriverVo.setDistance(currentDistance);
                list.add(nearByDriverVo);

            }

        }
        return list;
    }

    //司机赶往代驾起始点：更新订单地址到缓存
    @Override
    public Boolean updateOrderLocationToCache(UpdateOrderLocationForm updateOrderLocationForm) {

        OrderLocationVo orderLocationVo = new OrderLocationVo();
        orderLocationVo.setLongitude(updateOrderLocationForm.getLongitude());
        orderLocationVo.setLatitude(updateOrderLocationForm.getLatitude());

        String key = RedisConstant.UPDATE_ORDER_LOCATION + updateOrderLocationForm.getOrderId();
        redisTemplate.opsForValue().set(key,orderLocationVo);
        return true;
    }

    /**
     * 司机赶往代驾起始点：获取订单经纬度位置
     * @param orderId
     * @return
     */
    @Override
    public OrderLocationVo getCacheOrderLocation(Long orderId) {
        String key=RedisConstant.UPDATE_ORDER_LOCATION+orderId;
        OrderLocationVo orderLocationVo = (OrderLocationVo) redisTemplate.opsForValue().get(key);

        return orderLocationVo;
    }

    //代驾服务：获取订单服务最后一个位置信息
    @Override
    public OrderServiceLastLocationVo getOrderServiceLastLocation(Long orderId) {
        //查询MongoDB
        //查询条件：orderId
        //根据创建时间降序排序
        //最后一条数据
        Query query=new Query();
        query.addCriteria(Criteria.where("orderId").is(orderId));
        query.with(Sort.by(Sort.Order.desc("createTime")));
        query.limit(1);
        OrderServiceLocation orderServiceLocation =
                mongoTemplate.findOne(query, OrderServiceLocation.class);

        OrderServiceLastLocationVo orderServiceLastLocationVo = new OrderServiceLastLocationVo();
        BeanUtils.copyProperties(orderServiceLocation,orderServiceLastLocationVo);


        return orderServiceLastLocationVo;
    }

    /**
     * 代驾服务：计算订单实际里程
     * @param orderId
     * @return
     */
    @Override
    public BigDecimal calculateOrderRealDistance(Long orderId) {
        //1 根据订单id获取代价订单位置信息，根据创建时间排序 升序
        //查询mongodb
        //第一种方式
//        OrderServiceLocation orderServiceLocation = new OrderServiceLocation();
//        orderServiceLocation.setOrderId(orderId);
//        Example<OrderServiceLocation>example=Example.of(orderServiceLocation);
//
//        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
//        List<OrderServiceLocation> list = orderServiceLocationRepository.findAll(example, sort);
//
        //第二种方式 MongoDBRepository
        //1 查询方法名称以 get/find/read开头
        //2 后面查询字段名称
        //3 字段查询条件添加关键字 like OrderBy Asc
        //
        List<OrderServiceLocation> list =
                orderServiceLocationRepository.findByOrderIdOrderByCreateTimeAsc(orderId);

        //2 第一步查询返回订单位置信息list集合
        //把list集合遍历，得到每个位置信息，计算每个位置距离
        //把计算所有距离相加操作
        double realDistance=0;
        if(!CollectionUtils.isEmpty(list)){
            for(int i=0,size=list.size()-1;i<size;i++){
                OrderServiceLocation location1 = list.get(i);
                OrderServiceLocation location2 = list.get(i+1);
                //计算位置距离
                double distance = LocationUtil.getDistance(location1.getLatitude().doubleValue(),
                        location1.getLongitude().doubleValue(),
                        location2.getLatitude().doubleValue(),
                        location2.getLongitude().doubleValue());
                realDistance+=distance;
            }
        }
        //3 返回最终计算实际距离
        return new BigDecimal(realDistance);
    }
}
