package cn.van.daijia.rules.service;

import cn.van.daijia.model.form.rules.FeeRuleRequestForm;
import cn.van.daijia.model.vo.rules.FeeRuleResponseVo;

public interface FeeRuleService {

    //计算订单费用
    FeeRuleResponseVo calculateOrderFee(FeeRuleRequestForm feeRuleRequestForm);
}
