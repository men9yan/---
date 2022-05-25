package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.Indent;
import com.wukong.logisticsproject.model.Vehicle;
import com.wukong.logisticsproject.model.Waybill;
import com.wukong.logisticsproject.model.WaybillRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 运单记录表 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface IWaybillRecordService extends IService<WaybillRecord> {
    Integer addWayBillRecordInfo(WaybillRecord waybillRecord);

    String getWaybillReceiptInfoJson(Integer page, Integer count, HttpServletRequest request,String waybillState);

    List<Waybill> getWaybillReceipt(HttpServletRequest request,String waybillState);

    Map<String, Integer> getVehicleFullInfo(String vehicleNum);

    //查询不重复的运单数据
    String getFindWaybillRecordJson(Integer page, Integer count);

    List<WaybillRecord> getFindWaybillRecordById(String waybillId);
    //搜索
    String findWaybillRecordByParamJson(Integer page, Integer count,WaybillRecord waybillRecord);

}
