package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.*;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 运单表 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface IWaybillService extends IService<Waybill> {
    List<Waybill> getWaybillAllInfo(HttpServletRequest request);

    List<Waybill> getWaybillReceipt(HttpServletRequest request);

    String getWaybillAllInfoJson(Integer page, Integer count, HttpServletRequest request);

    List<Waybill> findWayBillByParam(String waybillId, String sendBranch, String destination, String customer, String phone);

    String findWayBillByParamJson(String waybillId, String sendBranch, String destination, String customer, String phone, Integer page, Integer rows);

    Integer updateWayBillById(Waybill waybill);

    Integer getInsertWayBill(Indent indent, String username);

    Integer getInsertAnyWayBill(Indent indent, String username);


    //查询已分派和签收的所有运单
    List<Waybill> getFindSignWaybillALl(String destinationBranch, String waybillState);

    String getFindSignWaybillALlJson(Integer page, Integer count, User loginUser, String waybillState);


    //修改运单签收信息
    Integer getUpdateWaybillById(Waybill waybill, User loginUser);

    //搜索功能
    List<Waybill> getFindWaybillByParam(Waybill waybill);

    String getFindWaybillByParamJson(Integer page, Integer count, Waybill waybill, User loginUser);


    //根据操作网点查询
    List<Waybill> findWaybillByOperateBranch(Waybill waybill);

    String getFindWaybillByOperateBranchJson(Integer page, Integer count, Waybill waybill, Staff staffInfo);
    //显示到件扫描的数据
    String getFindArriveWaybillByOperateBranchJson(Integer page, Integer count, Waybill waybill, Staff staffInfo);

    //发件扫描中回显数据
    Waybill getFindSendScanData(Waybill waybill, Staff staffInfo);

    Waybill getOneWayBillInfo(String wabillId);

    //发件扫描中下一站下拉框中显示的所有数据
    String getFindSendScanDataAllJson(Integer page, Integer count, Waybill waybill);

    //发件扫描中修改运单
    Integer getUpdateSendWaybillById(Waybill waybill, Staff staffInfo);

    //装车扫描中修改运单
    Integer getUpdateloadingWaybillById(Waybill waybill, Vehicle vehicle);

    //卸车扫描中修改运单
    Integer getUpdateunloadingWaybillById(Waybill waybill);

    //派件扫描中修改运单
    Integer getUpdateDeliveryWaybillById(Waybill waybill);

    //派件扫描中批量修改运单
    Integer getUpdateAnyDeliveryWaybillById(Waybill waybill);

    //到件扫描中修改运单
    Integer getUpdateArrivePieceById(Waybill waybill,Staff staffInfo);


}
