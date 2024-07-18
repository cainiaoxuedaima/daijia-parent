package cn.van.daijia.customer.service;

import cn.van.daijia.model.form.customer.UpdateWxPhoneForm;
import cn.van.daijia.model.vo.customer.CustomerLoginVo;

public interface CustomerService {

    //微信登录
    String login(String code);
    //获取用户信息
    CustomerLoginVo getCustomerLoginInfo(String token);
    //获取用户信息
    CustomerLoginVo getCustomerInfo(Long customerId);

    Boolean updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm);
}
