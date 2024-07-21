package cn.van.daijia.customer.service;

import cn.van.daijia.model.form.customer.ExpectOrderForm;
import cn.van.daijia.model.form.customer.SubmitOrderForm;
import cn.van.daijia.model.vo.customer.ExpectOrderVo;

public interface OrderService {


    ExpectOrderVo expectOrder(ExpectOrderForm expectOrderForm);

    // 提交订单
    Long submitOrder(SubmitOrderForm submitOrderForm);
}
