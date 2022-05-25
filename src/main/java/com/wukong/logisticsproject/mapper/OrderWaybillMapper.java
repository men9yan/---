package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.OrderWaybill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单运单关联表 Mapper 接口
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Repository
public interface OrderWaybillMapper extends BaseMapper<OrderWaybill> {

    //插入操作
    Integer insertOrderWaybill(OrderWaybill orderWaybill);
}
