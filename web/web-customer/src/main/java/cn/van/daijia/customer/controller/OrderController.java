package cn.van.daijia.customer.controller;

import cn.van.daijia.common.login.VanLogin;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.common.util.AuthContextHolder;
import cn.van.daijia.customer.service.OrderService;
import cn.van.daijia.model.form.customer.ExpectOrderForm;
import cn.van.daijia.model.form.customer.SubmitOrderForm;
import cn.van.daijia.model.vo.customer.ExpectOrderVo;
import cn.van.daijia.model.vo.order.CurrentOrderInfoVo;
import cn.van.daijia.model.vo.order.OrderInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "订单API接口管理")
@RestController
@RequestMapping("/order")
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderController {



    @Operation(summary = "乘客端查找当前订单")
    @VanLogin
    @GetMapping("/searchCustomerCurrentOrder")
    public Result<CurrentOrderInfoVo> searchCustomerCurrentOrder() {
        Long customerId = AuthContextHolder.getUserId();
        return Result.ok(orderService.searchCustomerCurrentOrder(customerId));
    }

    @Autowired
    private OrderService orderService;

    @Operation(summary = "预估订单数据")
    @VanLogin
    @PostMapping("/expectOrder")
    public Result<ExpectOrderVo> expectOrder(@RequestBody ExpectOrderForm expectOrderForm) {
        return Result.ok(orderService.expectOrder(expectOrderForm));
    }

    @Operation(summary = "乘客下单")
    @VanLogin
    @PostMapping("/submitOrder")
    public Result<Long> submitOrder(@RequestBody SubmitOrderForm submitOrderForm) {
        submitOrderForm.setCustomerId(AuthContextHolder.getUserId());
        return Result.ok(orderService.submitOrder(submitOrderForm));
    }

    @Operation(summary = "根据id查询订单")
    @VanLogin
    @GetMapping("/getOrderStatus/{orderId}")
    public Result<Integer>getOrderStatus(@PathVariable Long orderId){
        return Result.ok(orderService.getOrderStatus(orderId));
    }

    @Operation(summary = "获取订单信息")
    @VanLogin
    @GetMapping("/getOrderInfo/{orderId}")
    public Result<OrderInfoVo> getOrderInfo(@PathVariable Long orderId) {
        Long customerId = AuthContextHolder.getUserId();
        return Result.ok(orderService.getOrderInfo(orderId, customerId));
    }

}

