package cn.van.daijia.map.repository;

import cn.van.daijia.model.entity.map.OrderServiceLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderServiceLocationRepository
        extends MongoRepository<OrderServiceLocation, String> {


    // 根据订单ID查询订单服务位置
    List<OrderServiceLocation> findByOrderIdOrderByCreateTimeAsc(Long orderId);
}
