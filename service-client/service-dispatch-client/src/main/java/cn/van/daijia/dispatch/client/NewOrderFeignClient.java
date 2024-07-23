package cn.van.daijia.dispatch.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.vo.dispatch.NewOrderTaskVo;
import cn.van.daijia.model.vo.order.NewOrderDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "service-dispatch")
public interface NewOrderFeignClient {

    @PostMapping("/dispatch/newOrder/addAndStartTask")
    Result<Long>addAndStartTask(@RequestBody NewOrderTaskVo newOrderTaskVo);

    /**
     * 查询司机新订单数据
     *
     * @param driverId
     * @return
     */
    @GetMapping("/dispatch/newOrder/findNewOrderQueueData/{driverId}")
    Result<List<NewOrderDataVo>> findNewOrderQueueData(@PathVariable("driverId") Long driverId);

    /**
     * 清空新订单队列数据
     * @param driverId
     * @return
     */
    @GetMapping("/dispatch/newOrder/clearNewOrderQueueData/{driverId}")
    Result<Boolean> clearNewOrderQueueData(@PathVariable("driverId") Long driverId);
}