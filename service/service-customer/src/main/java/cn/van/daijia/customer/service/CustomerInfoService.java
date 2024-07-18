package cn.van.daijia.customer.service;

import cn.van.daijia.model.entity.customer.CustomerInfo;
import cn.van.daijia.model.vo.customer.CustomerLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CustomerInfoService extends IService<CustomerInfo> {

    Long login(String code);

    CustomerLoginVo getCustomerInfo(Long customerId);
}
