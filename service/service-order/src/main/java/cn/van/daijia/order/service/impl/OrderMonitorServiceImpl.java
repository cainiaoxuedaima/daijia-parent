package cn.van.daijia.order.service.impl;

import cn.van.daijia.model.entity.order.OrderMonitor;
import cn.van.daijia.model.entity.order.OrderMonitorRecord;
import cn.van.daijia.order.mapper.OrderMonitorMapper;
import cn.van.daijia.order.repository.OrderMonitorRecordRepository;
import cn.van.daijia.order.service.OrderMonitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderMonitorServiceImpl extends ServiceImpl<OrderMonitorMapper, OrderMonitor> implements OrderMonitorService {


    @Autowired
    private OrderMonitorRecordRepository orderMonitorRecordRepository;
    /**
     * 保存订单监控记录数据
     * @param orderMonitorRecord
     * @return
     */
    @Override
    public Boolean saveOrderMonitorRecord(OrderMonitorRecord orderMonitorRecord) {
        orderMonitorRecordRepository.save(orderMonitorRecord);
        return true;
    }



}
