package cn.van.daijia.customer.service.impl;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.customer.service.OrderService;
import cn.van.daijia.dispatch.client.NewOrderFeignClient;
import cn.van.daijia.map.client.MapFeignClient;
import cn.van.daijia.model.form.customer.ExpectOrderForm;
import cn.van.daijia.model.form.customer.SubmitOrderForm;
import cn.van.daijia.model.form.map.CalculateDrivingLineForm;
import cn.van.daijia.model.form.order.OrderInfoForm;
import cn.van.daijia.model.form.rules.FeeRuleRequestForm;
import cn.van.daijia.model.vo.customer.ExpectOrderVo;
import cn.van.daijia.model.vo.dispatch.NewOrderTaskVo;
import cn.van.daijia.model.vo.map.DrivingLineVo;
import cn.van.daijia.model.vo.rules.FeeRuleResponseVo;
import cn.van.daijia.order.client.OrderInfoFeignClient;
import cn.van.daijia.rules.client.FeeRuleFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MapFeignClient mapFeignClient;

    @Autowired
    private FeeRuleFeignClient feeRuleFeignClient;

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Autowired
    private NewOrderFeignClient newOrderFeignClient;

    //预估订单数据
    @Override
    public ExpectOrderVo expectOrder(ExpectOrderForm expectOrderForm) {
        //获取驾驶线路
        CalculateDrivingLineForm calculateDrivingLineForm = new CalculateDrivingLineForm();
        BeanUtils.copyProperties(expectOrderForm,calculateDrivingLineForm);
        Result<DrivingLineVo> drivingLineVoResult = mapFeignClient.calculateDrivingLine(calculateDrivingLineForm);
        DrivingLineVo drivingLineVo = drivingLineVoResult.getData();
        if(drivingLineVo==null){
            return null;
        }
        //获取订单费用
        FeeRuleRequestForm calculateOrderFeeForm = new FeeRuleRequestForm();
        if(drivingLineVo.getDistance() == null){
            drivingLineVo.setDistance(BigDecimal.valueOf(0));
        }
        calculateOrderFeeForm.setDistance(drivingLineVo.getDistance());
        calculateOrderFeeForm.setStartTime(new Date());
        calculateOrderFeeForm.setWaitMinute(0);
        Result<FeeRuleResponseVo> feeRuleResponseVoResult = feeRuleFeignClient.calculateOrderFee(calculateOrderFeeForm);
        FeeRuleResponseVo feeRuleResponseVo = feeRuleResponseVoResult.getData();

        //封装ExpectOrderVo
        ExpectOrderVo expectOrderVo = new ExpectOrderVo();
        expectOrderVo.setDrivingLineVo(drivingLineVo);
        expectOrderVo.setFeeRuleResponseVo(feeRuleResponseVo);
        return expectOrderVo;
    }

    @Override
    public Long submitOrder(SubmitOrderForm submitOrderForm) {
        //1 重新计算驾驶线路
        CalculateDrivingLineForm calculateDrivingLineForm=new CalculateDrivingLineForm();
        BeanUtils.copyProperties(submitOrderForm,calculateDrivingLineForm);
        Result<DrivingLineVo> drivingLineVoResult = mapFeignClient.calculateDrivingLine(calculateDrivingLineForm);
        DrivingLineVo drivingLineVo = drivingLineVoResult.getData();
        if(drivingLineVo==null){
            return null;
        }
        //获取订单费用
        FeeRuleRequestForm calculateOrderFeeForm = new FeeRuleRequestForm();
        if(drivingLineVo.getDistance() == null){
            drivingLineVo.setDistance(BigDecimal.valueOf(0));
        }
        calculateOrderFeeForm.setDistance(drivingLineVo.getDistance());
        calculateOrderFeeForm.setStartTime(new Date());
        calculateOrderFeeForm.setWaitMinute(0);
        Result<FeeRuleResponseVo> feeRuleResponseVoResult = feeRuleFeignClient.calculateOrderFee(calculateOrderFeeForm);
        FeeRuleResponseVo feeRuleResponseVo = feeRuleResponseVoResult.getData();



        //封装数据
        OrderInfoForm orderInfoForm = new OrderInfoForm();
        BeanUtils.copyProperties(submitOrderForm,orderInfoForm);
        orderInfoForm.setExpectDistance(drivingLineVo.getDistance());
        orderInfoForm.setExpectAmount(feeRuleResponseVo.getTotalAmount());
        Result<Long> orderInfoResult = orderInfoFeignClient.saveOrderInfo(orderInfoForm);
        Long orderId = orderInfoResult.getData();

        NewOrderTaskVo newOrderDispatchVo = new NewOrderTaskVo();
        newOrderDispatchVo.setOrderId(orderId);
        newOrderDispatchVo.setStartLocation(orderInfoForm.getStartLocation());
        newOrderDispatchVo.setStartPointLongitude(orderInfoForm.getStartPointLongitude());
        newOrderDispatchVo.setStartPointLatitude(orderInfoForm.getStartPointLatitude());
        newOrderDispatchVo.setEndLocation(orderInfoForm.getEndLocation());
        newOrderDispatchVo.setEndPointLongitude(orderInfoForm.getEndPointLongitude());
        newOrderDispatchVo.setEndPointLatitude(orderInfoForm.getEndPointLatitude());
        newOrderDispatchVo.setExpectAmount(orderInfoForm.getExpectAmount());
        newOrderDispatchVo.setExpectDistance(orderInfoForm.getExpectDistance());
        newOrderDispatchVo.setExpectTime(drivingLineVo.getDuration());
        newOrderDispatchVo.setFavourFee(orderInfoForm.getFavourFee());
        newOrderDispatchVo.setCreateTime(new Date());

        //远程调用
        Long jobId = newOrderFeignClient.addAndStartTask(newOrderDispatchVo).getData();
        //返回订单id
        return orderId;
    }

    //获取订单状态
    @Override
    public Integer getOrderStatus(Long orderId) {
        Result<Integer> orderStatus = orderInfoFeignClient.getOrderStatus(orderId);

        return orderStatus.getData();
    }


}
