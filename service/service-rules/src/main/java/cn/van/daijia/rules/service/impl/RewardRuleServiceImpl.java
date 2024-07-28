package cn.van.daijia.rules.service.impl;

import cn.van.daijia.model.form.rules.RewardRuleRequest;
import cn.van.daijia.model.form.rules.RewardRuleRequestForm;
import cn.van.daijia.model.vo.rules.RewardRuleResponse;
import cn.van.daijia.model.vo.rules.RewardRuleResponseVo;
import cn.van.daijia.rules.service.RewardRuleService;
import cn.van.daijia.rules.utils.DroolsHelper;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class RewardRuleServiceImpl implements RewardRuleService {


    private static final String RULES_CUSTOMER_RULES_DRL = "rules/RewardRule.drl";
    @Override
    public RewardRuleResponseVo calculateOrderRewardFee(RewardRuleRequestForm rewardRuleRequestForm) {
        //封装传入参数对象
        RewardRuleRequest rewardRuleRequest = new RewardRuleRequest();
        rewardRuleRequest.setOrderNum(rewardRuleRequestForm.getOrderNum());

        //创建规则引擎对象
        KieSession kieSession = DroolsHelper.loadForRule(RULES_CUSTOMER_RULES_DRL);

        //封装返回对象
        RewardRuleResponse rewardRuleResponse = new RewardRuleResponse();
        kieSession.setGlobal("rewardRuleResponse",rewardRuleResponse);

        //设置对象，处罚规则
        kieSession.insert(rewardRuleRequest);
        kieSession.fireAllRules();
        //释放kie
        kieSession.dispose();;

        //封装RewardRuleResponseVo
        RewardRuleResponseVo rewardRuleResponseVo = new RewardRuleResponseVo();
        rewardRuleResponseVo.setRewardAmount(rewardRuleResponse.getRewardAmount());
        return rewardRuleResponseVo;
    }
}
