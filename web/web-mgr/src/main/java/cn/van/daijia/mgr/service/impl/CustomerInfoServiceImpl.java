package cn.van.daijia.mgr.service.impl;

import cn.van.daijia.mgr.service.CustomerInfoService;
import cn.van.daijia.customer.client.CustomerInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerInfoServiceImpl implements CustomerInfoService {

	@Autowired
	private CustomerInfoFeignClient customerInfoFeignClient;



}
