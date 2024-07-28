package cn.van.daijia.rules.service;

import cn.van.daijia.model.form.rules.ProfitsharingRuleRequestForm;
import cn.van.daijia.model.vo.rules.ProfitsharingRuleResponseVo;

public interface ProfitsharingRuleService {

    //计算系统分账费用
    ProfitsharingRuleResponseVo calculateOrderProfitsharingFee(ProfitsharingRuleRequestForm profitsharingRuleRequestForm);
}
