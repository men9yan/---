package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.model.Waybill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wukong.logisticsproject.model.WaybillRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 运单表 Mapper 接口
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Repository
public interface WaybillMapper extends BaseMapper<Waybill> {
    Integer insertWaybill(Waybill waybill);

    //查询已分派和签收的所有运单
    List<Waybill> findSignWaybillALl(@Param("destinationBranch") String destinationBranch,@Param("waybillState") String waybillState);

    //修改运单信息
    Integer updateWaybillById(Waybill waybill);

    //查询派件员未签收的数量
    List<Waybill> findCountByDispatchStaff(@Param("dispatchStaff") Integer dispatchStaff,@Param("waybillState") String waybillState);

    //搜索功能
    List<Waybill> findWaybillByParam(Waybill waybill);

    //根据操作网点查询
    List<Waybill> findWaybillByOperateBranch(Waybill waybill);
}
