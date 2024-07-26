package cn.van.daijia.driver.service;

import cn.van.daijia.model.form.map.OrderServiceLocationForm;
import cn.van.daijia.model.form.map.UpdateDriverLocationForm;
import cn.van.daijia.model.form.map.UpdateOrderLocationForm;
import cn.van.daijia.model.vo.map.OrderLocationVo;

import java.util.List;

public interface LocationService {


    // 开启接单
    Boolean updateDriverLocation(UpdateDriverLocationForm updateDriverLocationForm);

    //司机赶往代驾起始点：更新订单地址到缓存
    Boolean updateOrderLocationToCache(UpdateOrderLocationForm updateOrderLocationForm);

    //批量保存代驾服务订单位置
    Boolean saveOrderServiceLocation(List<OrderServiceLocationForm> orderLocationServiceFormList);

    OrderLocationVo getCacheOrderLocation(Long orderId);
}
