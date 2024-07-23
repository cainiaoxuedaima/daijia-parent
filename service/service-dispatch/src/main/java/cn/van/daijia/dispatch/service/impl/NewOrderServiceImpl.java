package cn.van.daijia.dispatch.service.impl;

import cn.van.daijia.common.constant.RedisConstant;
import cn.van.daijia.dispatch.mapper.OrderJobMapper;
import cn.van.daijia.dispatch.service.NewOrderService;
import cn.van.daijia.dispatch.xxl.client.XxlJobClient;
import cn.van.daijia.map.client.LocationFeignClient;
import cn.van.daijia.model.entity.dispatch.OrderJob;
import cn.van.daijia.model.enums.OrderStatus;
import cn.van.daijia.model.form.map.SearchNearByDriverForm;
import cn.van.daijia.model.vo.dispatch.NewOrderTaskVo;
import cn.van.daijia.model.vo.map.NearByDriverVo;
import cn.van.daijia.model.vo.order.NewOrderDataVo;
import cn.van.daijia.order.client.OrderInfoFeignClient;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class NewOrderServiceImpl implements NewOrderService {

    @Autowired
    private XxlJobClient xxlJobClient;

    @Autowired
    private OrderJobMapper orderJobMapper;

    @Autowired
    private LocationFeignClient locationFeignClient;

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Long addAndStartTask(NewOrderTaskVo newOrderTaskVo) {
        //1 判断当前订单是否启动任务调度
        //根据订单id查询
        LambdaQueryWrapper<OrderJob>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(OrderJob::getOrderId,newOrderTaskVo.getOrderId());
        OrderJob orderJob = orderJobMapper.selectOne(wrapper);



        //2 没有启动，进行操作
        if(orderJob==null){
            Long jobId = xxlJobClient.addAndStart("newOrderTaskHandler",
                    "",
                    "0 */1 * * * ?",
                    "新创建订单调度：" + newOrderTaskVo.getOrderId());
            //记录任务调度信息
            orderJob =new OrderJob();
            orderJob.setOrderId(newOrderTaskVo.getOrderId());
            orderJob.setJobId(jobId);
            orderJob.setParameter(JSONObject.toJSONString(newOrderTaskVo));
            orderJobMapper.insert(orderJob);
        }


        return null;
    }


    /**
     * 执行任务
     * @param jobId
     */
    @Override
    public void executeTask(long jobId) {
        //1 根据jobid查询数据库，当前任务是否已经创建
        //如果没有创建，不往下执行了
        LambdaQueryWrapper<OrderJob>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(OrderJob::getJobId,jobId);
        OrderJob orderJob=orderJobMapper.selectOne(wrapper);
        if(orderJob==null){
            return ;
        }
        //2 查询订单状态，如果当前订单接单状态，继续执行，如果当前订单不是接单状态，停止任务调度
        //获取OrderJob里面的对象
        String jsonString = orderJob.getParameter();
        NewOrderTaskVo newOrderTaskVo = JSONObject.parseObject(jsonString, NewOrderTaskVo.class);

        //获取orderId
        Long orderId = newOrderTaskVo.getOrderId();

        Integer status = orderInfoFeignClient.getOrderStatus(orderId).getData();
        //不是接单状态 停止任务调度
        if(status.intValue()!= OrderStatus.WAITING_ACCEPT.getStatus().intValue()){
            xxlJobClient.stopJob(jobId);
            return ;
        }
        //3 远程调用：搜索附近满足条件可以接单的司机
        //4 远程调用之后，获取满足可以接单的司机集合
        SearchNearByDriverForm searchNearByDriverForm = new SearchNearByDriverForm();
        searchNearByDriverForm.setLongitude(newOrderTaskVo.getStartPointLongitude());
        searchNearByDriverForm.setLatitude(newOrderTaskVo.getStartPointLatitude());
        searchNearByDriverForm.setMileageDistance(newOrderTaskVo.getExpectDistance());
        List<NearByDriverVo> nearByDriverVoList = locationFeignClient.
                searchNearByDriver(searchNearByDriverForm).getData();

        //5 遍历司机集合，得到每个司机，为每个司机创建临时队列，存储新订单信息
        nearByDriverVoList.forEach(driver -> {
            //使用Redis的set类型
            //根据订单id生成key
            String repeatKey =
                    RedisConstant.DRIVER_ORDER_REPEAT_LIST+newOrderTaskVo.getOrderId();
            //记录司机id，防止重复推送
            Boolean isMember = redisTemplate.opsForSet().isMember(repeatKey, driver.getDriverId());
            if(!isMember) {
                //把订单信息推送给满足条件多个司机
                redisTemplate.opsForSet().add(repeatKey,driver.getDriverId());
                //过期时间：15分钟，超过15分钟没有接单自动取消
                redisTemplate.expire(repeatKey,
                        RedisConstant.DRIVER_ORDER_REPEAT_LIST_EXPIRES_TIME,
                        TimeUnit.MINUTES);

                NewOrderDataVo newOrderDataVo = new NewOrderDataVo();
                newOrderDataVo.setOrderId(newOrderTaskVo.getOrderId());
                newOrderDataVo.setStartLocation(newOrderTaskVo.getStartLocation());
                newOrderDataVo.setEndLocation(newOrderTaskVo.getEndLocation());
                newOrderDataVo.setExpectAmount(newOrderTaskVo.getExpectAmount());
                newOrderDataVo.setExpectDistance(newOrderTaskVo.getExpectDistance());
                newOrderDataVo.setExpectTime(newOrderTaskVo.getExpectTime());
                newOrderDataVo.setFavourFee(newOrderTaskVo.getFavourFee());
                newOrderDataVo.setDistance(driver.getDistance());
                newOrderDataVo.setCreateTime(newOrderTaskVo.getCreateTime());
                //新订单保存司机的临时队列，Redis里面List集合
                String key = RedisConstant.DRIVER_ORDER_TEMP_LIST+driver.getDriverId();
                redisTemplate.opsForList().leftPush(key,JSONObject.toJSONString(newOrderDataVo));
                //过期时间：1分钟
                redisTemplate.expire(key,RedisConstant.DRIVER_ORDER_TEMP_LIST_EXPIRES_TIME, TimeUnit.MINUTES);
            }
        });
    }


    /**
     * 获取司机临时队列
     * @param driverId
     * @return
     */
    @Override
    public List<NewOrderDataVo> findNewOrderQueueData(Long driverId) {

        List<NewOrderDataVo>list=new ArrayList<>();
        String key=RedisConstant.DRIVER_ORDER_TEMP_LIST+ driverId;
        Long size=redisTemplate.opsForList().size(key);
        if(size>0){
            for (int i = 0; i < size; i++) {
                String content = (String) redisTemplate.opsForList().leftPop(key);
                NewOrderDataVo newOrderDataVo=JSONObject.parseObject(content,NewOrderDataVo.class);
                list.add(newOrderDataVo);
            }
        }
        return list;
    }

    /**
     * 清除司机临时队列
     * @param driverId
     * @return
     */
    @Override
    public Boolean clearNewOrderQueueData(Long driverId) {
        redisTemplate.delete(RedisConstant.DRIVER_ORDER_TEMP_LIST+driverId);
        return true;
    }
}
