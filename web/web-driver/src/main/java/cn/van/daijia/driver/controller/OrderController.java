package cn.van.daijia.driver.controller;

import cn.van.daijia.common.login.VanLogin;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.util.AuthContextHolder;
import cn.van.daijia.driver.service.OrderService;
import cn.van.daijia.model.form.map.CalculateDrivingLineForm;
import cn.van.daijia.model.form.order.StartDriveForm;
import cn.van.daijia.model.form.order.UpdateOrderCartForm;
import cn.van.daijia.model.vo.driver.DriverInfoVo;
import cn.van.daijia.model.vo.map.DrivingLineVo;
import cn.van.daijia.model.vo.order.CurrentOrderInfoVo;
import cn.van.daijia.model.vo.order.NewOrderDataVo;
import cn.van.daijia.model.vo.order.OrderInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "订单API接口管理")
@RestController
@RequestMapping("/order")
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "根据id查询订单")
    @VanLogin
    @GetMapping("/getOrderStatus/{orderId}")
    public Result<Integer>getOrderStatus(@PathVariable Long orderId){
        return Result.ok(orderService.getOrderStatus(orderId));
    }

    /**
     * 查询司机新订单数据
     * @return
     */
    @Operation(summary = "查询司机新订单数据")
    @VanLogin
    @GetMapping("/findNewOrderQueueData")
    public Result<List<NewOrderDataVo>> findNewOrderQueueData() {
        Long driverId = AuthContextHolder.getUserId();
        return Result.ok(orderService.findNewOrderQueueData(driverId));
    }

    /**
     * 司机抢单
     * @param orderId
     * @return
     */
    @Operation(summary = "司机抢单")
    @VanLogin
    @GetMapping("/robNewOrder/{orderId}")
    public Result<Boolean>robNewOrder(@PathVariable Long orderId) {
        Long driverId = AuthContextHolder.getUserId();
        return Result.ok(orderService.robNewOrder(driverId, orderId));
    }

    @Operation(summary = "司机端查找当前订单")
    @VanLogin
    @GetMapping("/searchDriverCurrentOrder")
    public Result<CurrentOrderInfoVo> searchDriverCurrentOrder() {
        Long driverId = AuthContextHolder.getUserId();
        return Result.ok(orderService.searchDriverCurrentOrder(driverId));
    }

    @Operation(summary = "获取订单账单详细信息")
    @VanLogin
    @GetMapping("/getOrderInfo/{orderId}")
    public Result<OrderInfoVo> getOrderInfo(@PathVariable Long orderId) {
        Long driverId = AuthContextHolder.getUserId();
        return Result.ok(orderService.getOrderInfo(orderId, driverId));
    }
    @Operation(summary = "计算最佳驾驶路线")
    @VanLogin
    @PostMapping("/calculateDrivingLine")
    public Result<DrivingLineVo>calculateDrivingLine(@RequestBody CalculateDrivingLineForm calculateDrivingLineForm){
        return Result.ok(orderService.calculateDrivingLine(calculateDrivingLineForm));
    }

    @Operation(summary = "司机到达代驾起始地点")
    @VanLogin
    @GetMapping("/driverArriveStartLocation/{orderId}")
    public Result<Boolean> driverArriveStartLocation(@PathVariable Long orderId) {
        Long driverId = AuthContextHolder.getUserId();
        return Result.ok(orderService.driverArriveStartLocation(orderId, driverId));
    }

    @Operation(summary = "更新代驾车辆信息")
    @VanLogin
    @PostMapping("/updateOrderCart")
    public Result<Boolean> updateOrderCart(@RequestBody UpdateOrderCartForm updateOrderCartForm) {
        Long driverId = AuthContextHolder.getUserId();
        updateOrderCartForm.setDriverId(driverId);
        return Result.ok(orderService.updateOrderCart(updateOrderCartForm));
    }


    @Operation(summary = "开始代驾服务")
    @VanLogin
    @PostMapping("/startDrive")
    public Result<Boolean> startDrive(@RequestBody StartDriveForm startDriveForm) {
        Long driverId = AuthContextHolder.getUserId();
        startDriveForm.setDriverId(driverId);
        return Result.ok(orderService.startDrive(startDriveForm));
    }

}

