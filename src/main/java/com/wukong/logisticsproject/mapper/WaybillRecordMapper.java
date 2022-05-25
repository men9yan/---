package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.WaybillRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 运单记录表 Mapper 接口
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Repository
public interface WaybillRecordMapper extends BaseMapper<WaybillRecord> {


    Integer insertWaybillRecord(WaybillRecord waybillRecord);

    List<WaybillRecord> findWaybillRecord();

    List<WaybillRecord> findWaybillRecordById(String waybillId);

    List<WaybillRecord> findWaybillRecordByParam(WaybillRecord waybillRecord);

}
