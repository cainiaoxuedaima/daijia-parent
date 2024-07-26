package cn.van.daijia.order.service;

import cn.van.daijia.model.entity.order.OrderMonitor;
import cn.van.daijia.model.entity.order.OrderMonitorRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderMonitorService extends IService<OrderMonitor> {

    //保存订单监控记录数据
    Boolean saveOrderMonitorRecord(OrderMonitorRecord orderMonitorRecord);
}
