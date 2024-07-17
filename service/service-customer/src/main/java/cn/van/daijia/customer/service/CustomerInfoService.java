package cn.van.daijia.customer.service;

import cn.van.daijia.model.entity.customer.CustomerInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CustomerInfoService extends IService<CustomerInfo> {

    Long login(String code);
}
