package cn.van.daijia.rules.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.form.rules.RewardRuleRequestForm;
import cn.van.daijia.model.vo.rules.RewardRuleResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-rules")
public interface RewardRuleFeignClient {
    /**
     * 计算订单奖励费用
     * @param rewardRuleRequestForm
     * @return
     */
    @PostMapping("/rules/reward/calculateOrderRewardFee")
    Result<RewardRuleResponseVo> calculateOrderRewardFee(@RequestBody RewardRuleRequestForm rewardRuleRequestForm);

}