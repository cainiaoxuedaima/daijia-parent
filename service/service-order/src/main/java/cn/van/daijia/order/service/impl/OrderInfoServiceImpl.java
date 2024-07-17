package cn.van.daijia.order.service.impl;

import cn.van.daijia.model.entity.order.OrderInfo;
import cn.van.daijia.order.service.OrderInfoService;
import cn.van.daijia.order.mapper.OrderInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {


}
