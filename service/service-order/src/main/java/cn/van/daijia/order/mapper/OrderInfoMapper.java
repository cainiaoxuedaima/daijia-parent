package cn.van.daijia.order.mapper;

import cn.van.daijia.model.entity.order.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    //获取乘客订单分页列表
    IPage<OrderInfo> selectCustomerOrderPage(Page<OrderInfo> pageParam, Long customerId);

    //获取司机订单分页列表
    IPage<OrderInfo> selectDriverOrderPage(Page<OrderInfo> pageParam, Long driverId);
}
