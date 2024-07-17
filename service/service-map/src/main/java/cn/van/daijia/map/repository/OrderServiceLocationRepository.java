package cn.van.daijia.map.repository;

import cn.van.daijia.model.entity.map.OrderServiceLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderServiceLocationRepository extends MongoRepository<OrderServiceLocation, String> {

}
