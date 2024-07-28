package cn.van.daijia.rules.service.impl;

import cn.van.daijia.model.form.rules.ProfitsharingRuleRequest;
import cn.van.daijia.model.form.rules.ProfitsharingRuleRequestForm;
import cn.van.daijia.model.vo.rules.ProfitsharingRuleResponse;
import cn.van.daijia.model.vo.rules.ProfitsharingRuleResponseVo;
import cn.van.daijia.rules.mapper.ProfitsharingRuleMapper;
import cn.van.daijia.rules.service.ProfitsharingRuleService;
import cn.van.daijia.rules.utils.DroolsHelper;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProfitsharingRuleServiceImpl implements ProfitsharingRuleService {


    @Autowired
    private ProfitsharingRuleMapper rewardRulesMapper;

    private static final String RULES_CUSTOMER_RULES_DRL="rules/ProfitsharingRule.drl";
    @Override
    public ProfitsharingRuleResponseVo calculateOrderProfitsharingFee(ProfitsharingRuleRequestForm profitsharingRuleRequestForm) {
        //传入参数对象封装
        ProfitsharingRuleRequest request = new ProfitsharingRuleRequest();
        request.setOrderAmount(profitsharingRuleRequestForm.getOrderAmount());
        request.setOrderNum(profitsharingRuleRequestForm.getOrderNum());

        //创建kieSession对象
        KieSession kieSession = DroolsHelper.loadForRule(RULES_CUSTOMER_RULES_DRL);

        //封装返回对象
        ProfitsharingRuleResponse response = new ProfitsharingRuleResponse();
        kieSession.setGlobal("profitsharingRuleResponse",response);

        //触发规则，返回vo对象
        kieSession.insert(request);
        kieSession.fireAllRules();
        kieSession.dispose();

        ProfitsharingRuleResponseVo profitsharingRuleResponseVo = new ProfitsharingRuleResponseVo();
        BeanUtils.copyProperties(response,profitsharingRuleResponseVo);
        return profitsharingRuleResponseVo;
    }
}
