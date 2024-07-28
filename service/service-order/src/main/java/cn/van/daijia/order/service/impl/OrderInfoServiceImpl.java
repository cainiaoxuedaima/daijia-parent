package cn.van.daijia.order.service.impl;

import cn.van.daijia.common.constant.RedisConstant;
import cn.van.daijia.common.execption.GuiguException;
import cn.van.daijia.common.result.ResultCodeEnum;
import cn.van.daijia.model.entity.order.OrderBill;
import cn.van.daijia.model.entity.order.OrderInfo;
import cn.van.daijia.model.entity.order.OrderProfitsharing;
import cn.van.daijia.model.entity.order.OrderStatusLog;
import cn.van.daijia.model.enums.OrderStatus;
import cn.van.daijia.model.form.order.OrderInfoForm;
import cn.van.daijia.model.form.order.StartDriveForm;
import cn.van.daijia.model.form.order.UpdateOrderBillForm;
import cn.van.daijia.model.form.order.UpdateOrderCartForm;
import cn.van.daijia.model.vo.order.CurrentOrderInfoVo;
import cn.van.daijia.order.mapper.OrderBillMapper;
import cn.van.daijia.order.mapper.OrderProfitsharingMapper;
import cn.van.daijia.order.mapper.OrderStatusLogMapper;
import cn.van.daijia.order.service.OrderInfoService;
import cn.van.daijia.order.mapper.OrderInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private OrderStatusLogMapper orderStatusLogMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private RedissonClient redissonClient;

    @Autowired
    private OrderBillMapper orderBillMapper ;

    @Autowired
    private OrderProfitsharingMapper orderProfitsharingMapper;

    //乘客下单
    @Override
    public Long saveOrderInfo(OrderInfoForm orderInfoForm) {
        //order_info添加订单数据
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderInfoForm,orderInfo);
        //订单号
        String orderNo = UUID.randomUUID().toString().replaceAll("-","");
        orderInfo.setOrderNo(orderNo);
        //订单状态
        orderInfo.setStatus(OrderStatus.WAITING_ACCEPT.getStatus());
        orderInfoMapper.insert(orderInfo);

        //记录日志
        this.log(orderInfo.getId(),orderInfo.getStatus());

        //向redis添加标识
        //接单标识，标识不存在了说明不在等待接单状态了
        redisTemplate.opsForValue().set(RedisConstant.ORDER_ACCEPT_MARK,
                "0", RedisConstant.ORDER_ACCEPT_MARK_EXPIRES_TIME, TimeUnit.MINUTES);

        return orderInfo.getId();
    }
    //生成订单之后，发送延迟消息
//    private void sendDelayMessage(Long orderId) {
//        try{
//            //1 创建队列
//            RBlockingQueue<Object> blockingDueue = redissonClient.getBlockingQueue("queue_cancel");
//
//            //2 把创建队列放到延迟队列里面
//            RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDueue);
//
//            //3 发送消息到延迟队列里面
//            //设置过期时间
//            delayedQueue.offer(orderId.toString(),15,TimeUnit.MINUTES);
//
//        }catch (Exception e) {
//            e.printStackTrace();
//            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
//        }
//    }

    /**
     * 根据订单id获取订单状态
     * @param orderId
     * @return
     */
    @Override
    public Integer getOrderStatus(Long orderId) {
        //sql语句 select status from order_info where id=?
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper();
        wrapper.eq(OrderInfo::getId,orderId);
        wrapper.select(OrderInfo::getStatus);
        //调用mapper方法
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        //订单不存在
        if(orderInfo==null){
            return OrderStatus.NULL_ORDER.getStatus();
        }
        return orderInfo.getStatus();
    }


//    //司机抢单
//    @Override
//    public Boolean robNewOrder(Long driverId, Long orderId) {
//        //判断订单是否存在,通过redis，减少数据库压力
//        if(!redisTemplate.hasKey(RedisConstant.ORDER_ACCEPT_MARK)){
//            //抢单失败
//            throw new GuiguException(ResultCodeEnum.COB_NEW_ORDER_FAIL);
//
//        }
//        //司机抢单
//        //修改order_info表订单状态值2：已经接单
//        //修改条件：根据订单id+司机id+司机接单时间
//        LambdaQueryWrapper<OrderInfo>wrapper=new LambdaQueryWrapper<>();
//        wrapper.eq(OrderInfo::getId,orderId);
//        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
//        //设置
//        orderInfo.setStatus(OrderStatus.ACCEPTED.getStatus());
//        orderInfo.setDriverId(driverId);
//        orderInfo.setAcceptTime(new Date());
//        //调用方法修改
//        int rows = orderInfoMapper.updateById(orderInfo);
//        if(rows!=1){
//            throw new GuiguException(ResultCodeEnum.COB_NEW_ORDER_FAIL);
//        }
//
//        //删除抢单标识
//        redisTemplate.delete(RedisConstant.ORDER_ACCEPT_MARK);
//
//
//        return true;
//    }


    //Redisson分布式锁
    //司机抢单
    @Override
    public Boolean robNewOrder(Long driverId, Long orderId) {
        //判断订单是否存在，通过Redis，减少数据库压力
        if(!redisTemplate.hasKey(RedisConstant.ORDER_ACCEPT_MARK)) {
            //抢单失败
            throw new GuiguException(ResultCodeEnum.COB_NEW_ORDER_FAIL);
        }

        //创建锁
        RLock lock = redissonClient.getLock(RedisConstant.ROB_NEW_ORDER_LOCK + orderId);

        try {
            //获取锁
            boolean flag = lock.tryLock(RedisConstant.ROB_NEW_ORDER_LOCK_WAIT_TIME,RedisConstant.ROB_NEW_ORDER_LOCK_LEASE_TIME, TimeUnit.SECONDS);
            if(flag) {
                if(!redisTemplate.hasKey(RedisConstant.ORDER_ACCEPT_MARK)) {
                    //抢单失败
                    throw new GuiguException(ResultCodeEnum.COB_NEW_ORDER_FAIL);
                }
                //司机抢单
                //修改order_info表订单状态值2：已经接单 + 司机id + 司机接单时间
                //修改条件：根据订单id
                LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(OrderInfo::getId,orderId);
                OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
                //设置
                orderInfo.setStatus(OrderStatus.ACCEPTED.getStatus());
                orderInfo.setDriverId(driverId);
                orderInfo.setAcceptTime(new Date());
                //调用方法修改
                int rows = orderInfoMapper.updateById(orderInfo);
                if(rows != 1) {
                    //抢单失败
                    throw new GuiguException(ResultCodeEnum.COB_NEW_ORDER_FAIL);
                }

                //删除抢单标识
                redisTemplate.delete(RedisConstant.ORDER_ACCEPT_MARK);
            }
        }catch (Exception e) {
            //抢单失败
            throw new GuiguException(ResultCodeEnum.COB_NEW_ORDER_FAIL);
        }finally {
            //释放
            if(lock.isLocked()) {
                lock.unlock();
            }
        }
        return true;
    }

    /**
     * 乘客端查找当前订单
     * @param customerId
     * @return
     */
    @Override
    public CurrentOrderInfoVo searchCustomerCurrentOrder(Long customerId) {
        //封装条件
        //乘客id
        LambdaQueryWrapper<OrderInfo>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getCustomerId,customerId);

        //各种状态
        Integer[] statusArray = {
                OrderStatus.ACCEPTED.getStatus(),
                OrderStatus.DRIVER_ARRIVED.getStatus(),
                OrderStatus.UPDATE_CART_INFO.getStatus(),
                OrderStatus.START_SERVICE.getStatus(),
                OrderStatus.END_SERVICE.getStatus(),
                OrderStatus.UNPAID.getStatus()
        };
        wrapper.in(OrderInfo::getStatus,statusArray);

        //获取最新一条记录
        wrapper.orderByDesc(OrderInfo::getId);
        wrapper.last("limit 1");

        //调用方法
        OrderInfo orderInfo=orderInfoMapper.selectOne(wrapper);

        //封装到currentOrderInfoVo
        CurrentOrderInfoVo currentOrderInfoVo = new CurrentOrderInfoVo();
        if(orderInfo!=null){
            currentOrderInfoVo.setOrderId(orderInfo.getId());
            currentOrderInfoVo.setStatus(orderInfo.getStatus());
            currentOrderInfoVo.setIsHasCurrentOrder(true);
        }else{
            currentOrderInfoVo.setIsHasCurrentOrder(false);
        }

        return null;
    }

    /**
     * 司机端查找当前订单
     * @param driverId
     * @return
     */
    //司机端查找当前订单
    @Override
    public CurrentOrderInfoVo searchDriverCurrentOrder(Long driverId) {
        //封装条件
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getDriverId,driverId);
        Integer[] statusArray = {
                OrderStatus.ACCEPTED.getStatus(),
                OrderStatus.DRIVER_ARRIVED.getStatus(),
                OrderStatus.UPDATE_CART_INFO.getStatus(),
                OrderStatus.START_SERVICE.getStatus(),
                OrderStatus.END_SERVICE.getStatus()
        };
        wrapper.in(OrderInfo::getStatus,statusArray);
        wrapper.orderByDesc(OrderInfo::getId);
        wrapper.last(" limit 1");
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        //封装到vo
        CurrentOrderInfoVo currentOrderInfoVo = new CurrentOrderInfoVo();
        if(null != orderInfo) {
            currentOrderInfoVo.setStatus(orderInfo.getStatus());
            currentOrderInfoVo.setOrderId(orderInfo.getId());
            currentOrderInfoVo.setIsHasCurrentOrder(true);
        } else {
            currentOrderInfoVo.setIsHasCurrentOrder(false);
        }
        return currentOrderInfoVo;
    }

    /**
     * 司机到达起始点
     * @param orderId
     * @param driverId
     * @return
     */
    @Override
    public Boolean driverArriveStartLocation(Long orderId, Long driverId) {
        //更新订单状态和到达时间，条件：orderId+driverId
        LambdaQueryWrapper<OrderInfo>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getId,orderId);
        wrapper.eq(OrderInfo::getDriverId,driverId);

        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setStatus(OrderStatus.DRIVER_ARRIVED.getStatus());
        orderInfo.setArriveTime(new Date());

        int rows = orderInfoMapper.update(orderInfo, wrapper);
        if(rows==1){
            return true;
        }else{
            throw new GuiguException(ResultCodeEnum.UPDATE_ERROR);
        }
    }


    /**
     * 更新代驾车辆信息
     * @param updateOrderCartForm
     * @return
     */
    @Override
    public Boolean updateOrderCart(UpdateOrderCartForm updateOrderCartForm) {
       LambdaQueryWrapper<OrderInfo>wrapper=new LambdaQueryWrapper<>();
       wrapper.eq(OrderInfo::getId,updateOrderCartForm.getOrderId());
       wrapper.eq(OrderInfo::getDriverId,updateOrderCartForm.getDriverId());
       OrderInfo orderInfo=new OrderInfo();
       BeanUtils.copyProperties(updateOrderCartForm,orderInfo);
       orderInfo.setStatus(OrderStatus.UPDATE_CART_INFO.getStatus());
        int rows = orderInfoMapper.update(orderInfo, wrapper);
        if(rows==1){
            return true;
        }else{
            throw new GuiguException(ResultCodeEnum.UPDATE_ERROR);
        }
    }

    /**
     * 开始代驾服务
     * @param startDriveForm
     * @return
     */
    @Override
    public Boolean startDriver(StartDriveForm startDriveForm) {
        //根据订单id + 司机id 更新订单状态和开始代价时间
        LambdaQueryWrapper<OrderInfo>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getId,startDriveForm.getOrderId());
        wrapper.eq(OrderInfo::getDriverId,startDriveForm.getDriverId());
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus(OrderStatus.START_SERVICE.getStatus());
        orderInfo.setStartServiceTime(new Date());
        int rows = orderInfoMapper.update(orderInfo, wrapper);
        if(rows==1){
            return true;
        }else{
            throw new GuiguException(ResultCodeEnum.UPDATE_ERROR);
        }
    }

    //根据时间段获取订单数
    @Override
    public Long getOrderNumByTime(String startTime, String endTime) {
        LambdaQueryWrapper<OrderInfo> wrapper=new LambdaQueryWrapper<>();
        wrapper.ge(OrderInfo::getStartServiceTime,startTime);
        wrapper.lt(OrderInfo::getStartServiceTime,endTime);

        Long count = orderInfoMapper.selectCount(wrapper);
        return count;
    }


    //结束代驾服务更新订单账单
    @Override
    public Boolean endDrive(UpdateOrderBillForm updateOrderBillForm) {
        LambdaQueryWrapper<OrderInfo>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getId,updateOrderBillForm.getOrderId());
        wrapper.eq(OrderInfo::getDriverId,updateOrderBillForm.getDriverId());

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus(OrderStatus.END_SERVICE.getStatus());
        orderInfo.setRealAmount(updateOrderBillForm.getTotalAmount());
        orderInfo.setFavourFee(updateOrderBillForm.getFavourFee());
        orderInfo.setRealDistance(updateOrderBillForm.getRealDistance());
        orderInfo.setEndServiceTime(new Date());

        int rows = orderInfoMapper.update(orderInfo, wrapper);
        if(rows==1){
            //添加账单数据
            OrderBill orderBill = new OrderBill();
            BeanUtils.copyProperties(updateOrderBillForm,orderBill);
            orderBill.setOrderId(updateOrderBillForm.getOrderId());
            orderBill.setPayAmount(updateOrderBillForm.getTotalAmount());

            //添加分账信息
            OrderProfitsharing orderProfitsharing = new OrderProfitsharing();
            BeanUtils.copyProperties(updateOrderBillForm,orderProfitsharing);
            orderProfitsharing.setOrderId(updateOrderBillForm.getOrderId());
            orderProfitsharing.setRuleId(updateOrderBillForm.getProfitsharingRuleId());
            orderProfitsharing.setStatus(1);
            orderProfitsharingMapper.insert(orderProfitsharing);
        }else{
            throw new GuiguException(ResultCodeEnum.UPDATE_ERROR);
        }
        return true;
    }

    public void log(Long orderId, Integer status) {
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        orderStatusLog.setOrderId(orderId);
        orderStatusLog.setOrderStatus(status);
        orderStatusLog.setOperateTime(new Date());
        orderStatusLogMapper.insert(orderStatusLog);
    }

}
