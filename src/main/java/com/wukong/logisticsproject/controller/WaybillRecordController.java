package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.ex.ParameterValidationException;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.service.IBranchService;
import com.wukong.logisticsproject.service.IVehicleService;
import com.wukong.logisticsproject.service.IWaybillRecordService;
import com.wukong.logisticsproject.service.IWaybillService;
import com.wukong.logisticsproject.utils.DateUtils;
import com.wukong.logisticsproject.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 运单记录表 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@RestController
@Slf4j
public class WaybillRecordController {
    @Autowired
    private IBranchService branchService;

    @Autowired
    private IWaybillRecordService waybillRecordService;

    @Autowired
    private IWaybillService waybillService;

    @Autowired
    private IVehicleService vehicleService;

    /**
     * 获取运单信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/getWaybillReceipt")
    public String getWaybillReceipt(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        String waybillState = request.getParameter("waybillState");

        return waybillRecordService.getWaybillReceiptInfoJson(Integer.parseInt(page), Integer.parseInt(rows), request, waybillState);

    }


    /*
     * 收件：添加功能
     */
    @PostMapping(value = "/insertReceipt")
    public R insertReceipt(@Validated(VerifySeq.class) WaybillRecord waybillRecord, BindingResult bindingResult, HttpServletRequest request) throws ParseException {

        String waybillState = request.getParameter("waybillState");
        System.out.println("....." + waybillState);
        Date date = DateUtils.string2Date(waybillRecord.getOperateTimeStr()); //时间类型转换
        waybillRecord.setOperateTime(DateUtils.date2String(date));
        waybillRecord.setWaybillState(waybillState);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }
        Waybill waybillInfo = new Waybill();
        waybillInfo.setWaybillId(waybillRecord.getWaybillId());

        waybillInfo.setWaybillState(waybillState);
        if (!request.getParameter("weight").isEmpty()) {
            waybillInfo.setWeight(Integer.parseInt(request.getParameter("weight")));
        }
        //waybillRecordService.addWayBillRecordInfo(waybillRecord);
        waybillService.updateWayBillById(waybillInfo);
        return R.ok();

    }

    /*
    发车判断：是否装满
     */
    @PostMapping(value = "/DepartJudge")
    public Map<String, Integer> DepartJudge(@Validated(VerifySeq.class) WaybillRecord waybillRecord) {
        String[] waybillIdArray = waybillRecord.getWaybillId().split(",");
        Waybill oneWayBillInfo = waybillService.getOneWayBillInfo(waybillIdArray[0]);
        Map<String, Integer> dataMap = waybillRecordService.getVehicleFullInfo(oneWayBillInfo.getVehicleNum());
        return dataMap;
    }

    /*
     * 发车：添加功能
     */
    @PostMapping(value = "/insertDepart")
    public R insertDepart(@Validated(VerifySeq.class) WaybillRecord waybillRecord, BindingResult bindingResult, HttpServletRequest request) throws ParseException {
        System.out.println(waybillRecord.toString());
        Date date = DateUtils.string2Date(waybillRecord.getOperateTimeStr()); //时间类型转换
        String[] waybillIdArray = waybillRecord.getWaybillId().split(",");
        Waybill oneWayBillInfo = waybillService.getOneWayBillInfo(waybillIdArray[0]);
        //填充车辆数据
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleState("运输");
        String staffState = "忙碌";
        vehicle.setVehicleNum(oneWayBillInfo.getVehicleNum());
        //填充订单数据
        Waybill waybillInfo = new Waybill();
        waybillInfo.setWaybillId(waybillRecord.getWaybillId());
        waybillInfo.setWaybillState(waybillRecord.getWaybillState());
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }
        for (String waybillId : waybillIdArray) {
            waybillRecord.setWaybillId(waybillId);
            waybillRecord.setOperateTime(DateUtils.date2String(date));
            waybillInfo.setWaybillId(waybillId);
            waybillRecordService.addWayBillRecordInfo(waybillRecord);
            waybillService.updateWayBillById(waybillInfo);
        }
        vehicleService.UpdateVehicleState(vehicle);
        return R.ok();

    }


    /**
     * 到车扫描中查询所有数据
     * @param waybill
     * @param request
     * @return
     */
    @PostMapping(value = "/getFindArriveCarWaybillByBranchJson")
    public String getFindArriveCarWaybillByBranchJson(Waybill waybill, HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        waybill.setWaybillState("4");
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getFindWaybillByOperateBranchJson(Integer.parseInt(page),Integer.parseInt(rows),waybill,staffInfo);

    }

    /**
     * 到车扫描修改
     * @param waybillRecord
     * @param bindingResult
     * @param request
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "/updateArriveCarScan")
    public R updateArriveCarScan(@Validated(VerifySeq.class) WaybillRecord waybillRecord, BindingResult bindingResult, HttpServletRequest request) throws ParseException {
        System.out.println(waybillRecord.toString());
        Date date = DateUtils.string2Date(waybillRecord.getOperateTimeStr()); //时间类型转换
        String[] waybillIdArray = waybillRecord.getWaybillId().split(",");
        Waybill oneWayBillInfo = waybillService.getOneWayBillInfo(waybillIdArray[0]);
        //填充车辆数据
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleState("空闲");
        vehicle.setVehicleNum(oneWayBillInfo.getVehicleNum());

        //填充订单数据
        Waybill waybillInfo = new Waybill();
        waybillInfo.setWaybillId(waybillRecord.getWaybillId());
        waybillInfo.setWaybillState(waybillRecord.getWaybillState());
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }
        for (String waybillId : waybillIdArray) {
            waybillRecord.setWaybillId(waybillId);
            waybillRecord.setOperateTime(DateUtils.date2String(date));
            waybillInfo.setWaybillId(waybillId);
            waybillRecordService.addWayBillRecordInfo(waybillRecord);
            waybillService.updateWayBillById(waybillInfo);
        }
        vehicleService.UpdateVehicleState(vehicle);
        return R.ok();

    }

    /**
     * 查询不重复的运单记录
     * @param request
     * @return
     */
    @PostMapping(value = "/getFindWaybillRecordJson")
    public String getFindWaybillRecordJson(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillRecordService.getFindWaybillRecordJson(Integer.parseInt(page),Integer.parseInt(rows));

    }


    /**
     * 查询指定运单所有记录
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFindWaybillRecordById", method = {RequestMethod.POST})
    public R getFindWaybillRecordById(HttpServletRequest request) {
        String waybillId = request.getParameter("waybillId");
        return R.ok(waybillRecordService.getFindWaybillRecordById(waybillId));
    }

    /**
     * 搜索功能
     * @param request
     * @param waybillRecord
     * @return
     */
    @PostMapping(value = "/findWaybillRecordByParamJson")
    public String findWaybillRecordByParamJson(HttpServletRequest request,WaybillRecord waybillRecord) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillRecordService.findWaybillRecordByParamJson(Integer.parseInt(page),Integer.parseInt(rows),waybillRecord);

    }

}
