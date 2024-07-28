package cn.van.daijia.rules.service;

import cn.van.daijia.model.form.rules.RewardRuleRequestForm;
import cn.van.daijia.model.vo.rules.RewardRuleResponseVo;

public interface RewardRuleService {

    //计算订单奖励费用
    RewardRuleResponseVo calculateOrderRewardFee(RewardRuleRequestForm rewardRuleRequestForm);
}
