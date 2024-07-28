package cn.van.daijia.rules.controller;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.form.rules.ProfitsharingRuleRequestForm;
import cn.van.daijia.model.vo.rules.ProfitsharingRuleResponseVo;
import cn.van.daijia.rules.service.ProfitsharingRuleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rules/profitsharing")
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProfitsharingRuleController {
    @Autowired
    private ProfitsharingRuleService profitsharingRuleService;

    @Operation(summary = "计算系统分账费用")
    @PostMapping("/calculateOrderProfitsharingFee")
    public Result<ProfitsharingRuleResponseVo> calculateOrderProfitsharingFee(@RequestBody ProfitsharingRuleRequestForm profitsharingRuleRequestForm) {
        return Result.ok(profitsharingRuleService.calculateOrderProfitsharingFee(profitsharingRuleRequestForm));
    }

}

