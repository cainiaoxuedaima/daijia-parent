package cn.van.daijia.order.controller;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.form.order.OrderInfoForm;
import cn.van.daijia.order.service.OrderInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "订单API接口管理")
@RestController
@RequestMapping(value="/order/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderInfoController {


    @Autowired
    private OrderInfoService orderInfoService;

    @Operation(summary = "保存订单信息")
    @PostMapping("/saveOrderInfo")
    public Result<Long>saveOrderInfo(@RequestBody OrderInfoForm orderInfoForm){
        return Result.ok(orderInfoService.saveOrderInfo(orderInfoForm));
    }

}

