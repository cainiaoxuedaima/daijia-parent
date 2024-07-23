package cn.van.daijia.dispatch.service;

import cn.van.daijia.model.vo.dispatch.NewOrderTaskVo;
import cn.van.daijia.model.vo.order.NewOrderDataVo;

import java.util.List;

public interface NewOrderService {

    // 添加任务并启动
    Long addAndStartTask(NewOrderTaskVo newOrderTaskVo);

    // 执行任务
    void executeTask(long jobId);

    // 查询新订单数据
    List<NewOrderDataVo> findNewOrderQueueData(Long driverId);

    // 清空新订单数据
    Boolean clearNewOrderQueueData(Long driverId);
}
