package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.ex.ParameterValidationException;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.service.IWaybillService;
import com.wukong.logisticsproject.utils.DateUtils;
import com.wukong.logisticsproject.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 运单表 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Slf4j
@RestController
public class WaybillController {

    @Autowired
    private IWaybillService waybillService;


    /**
     * 获取运单信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/getWaybillAllInfo")
    public String getWaybillAllInfo(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getWaybillAllInfoJson(Integer.parseInt(page), Integer.parseInt(rows),request);

    }

    /**
     * 搜索
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/findWayBillByParam")
    public String findWayBillByParam(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        String waybillId = request.getParameter("waybillId");
        String sendBranch = request.getParameter("sendBranch");
        String destination = request.getParameter("destination");
        String customer = request.getParameter("customer");
        String phone = request.getParameter("phone");
        return waybillService.findWayBillByParamJson(waybillId, sendBranch, destination, customer, phone, Integer.parseInt(page), Integer.parseInt(rows));
    }

    /**
     * 订单修改
     * @param waybill
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/updateWayBillById")
    public R updateWayBillById(@Validated(VerifySeq.class) Waybill waybill, BindingResult bindingResult) {
        System.out.println(waybill);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }

        return R.ok(waybillService.updateWayBillById(waybill));
    }


    /**
     * 一对一生成运单
     * @param indent
     * @param bindingResult
     * @param request
     * @return
     */
    @PostMapping(value = "/insertWayBillByParam")
    public R insertWayBillByParam(@Validated(VerifySeq.class) Indent indent, BindingResult bindingResult, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        System.out.println(indent);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }

        return R.ok(waybillService.getInsertWayBill(indent,loginUser.getUsername()));
    }


    /**
     * 多对一生成运单
     * @param indent
     * @param bindingResult
     * @param request
     * @return
     */
    @PostMapping(value = "/insertAnyWayBillByParam")
    public R insertAnyWayBillByParam(@Validated(VerifySeq.class) Indent indent, BindingResult bindingResult,HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        System.out.println(indent);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }

        return R.ok(waybillService.getInsertAnyWayBill(indent,loginUser.getUsername()));
    }


    /**
     * 查询所有状态为已派件的运单
     * @param request
     * @return
     */
    @PostMapping(value = "/getFindDeliveryWaybillALl")
    public String getFindDeliveryWaybillALl(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getFindSignWaybillALlJson(Integer.parseInt(page),Integer.parseInt(rows),loginUser,"8");

    }

    /**
     * 查询所有状态为已签收的运单
     * @param request
     * @return
     */
    @PostMapping(value = "/getFindSignWaybillALl")
    public String getFindSignWaybillALl(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getFindSignWaybillALlJson(Integer.parseInt(page),Integer.parseInt(rows),loginUser,"9");

    }


    /**
     * 签收时修改运单
     *
     * @return
     */
    @PostMapping(value = "/getUpdateWayBillById")
    public R getUpdateWayBillById( Waybill waybill, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        System.out.println("!!1"+waybill);
        R s = R.ok(waybillService.getUpdateWaybillById(waybill,loginUser));
        System.out.println(s);
        return s;

    }


    /**
     * 搜索已分派的数据
     * @param waybill
     * @param request
     * @return
     */
    @PostMapping(value = "/FindAssignWaybillALl")
    public String FindAssignWaybillALl(Waybill waybill, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        waybill.setWaybillState("8");
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getFindWaybillByParamJson(Integer.parseInt(page),Integer.parseInt(rows),waybill,loginUser);

    }


    /**
     * 搜索已签收的数据
     * @param waybill
     * @param request
     * @return
     */
    @PostMapping(value = "/FindSignWaybillALl")
    public String FindSignWaybillALl(Waybill waybill, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        waybill.setWaybillState("9");
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getFindWaybillByParamJson(Integer.parseInt(page),Integer.parseInt(rows),waybill,loginUser);

    }


    /**
     * 发件扫描中查询所有数据
     * @param waybill
     * @param request
     * @return
     */
    @PostMapping(value = "/getFindWaybillByBranchJson")
    public String getFindWaybillByBranchJson(Waybill waybill, HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        waybill.setWaybillState("1");
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getFindWaybillByOperateBranchJson(Integer.parseInt(page),Integer.parseInt(rows),waybill,staffInfo);

    }


    /**
     * 回显 下一站网点的信息
     * @param waybill
     * @param request
     * @return
     */
    @PostMapping(value = "/getFindSendScanData")
    public R getFindSendScanData( Waybill waybill, HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        System.out.println("!!1"+waybill);

        return R.ok(waybillService.getFindSendScanData(waybill,staffInfo));
    }


    /**
     * 发件扫描中查询同等级的所有数据
     * @param waybill
     * @param request
     * @return
     */
    @PostMapping(value = "/getFindSendScanDataAll")
    public String getFindSendScanDataAll( Waybill waybill, HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getFindSendScanDataAllJson(Integer.parseInt(page),Integer.parseInt(rows),waybill);
    }

//------------------------------------------------------------------------------------------------------------------------------
    /**
     * 发件扫描中修改运单
     * @param waybill
     * @return
     */
    @PostMapping(value = "/getUpdateSendWaybillById")
    public R getUpdateSendWaybillById( Waybill waybill, HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        System.out.println("!!1"+waybill);

        return R.ok(waybillService.getUpdateSendWaybillById(waybill,staffInfo));

    }





    /**
     *装车扫描中查询所有数据
     * @param waybill
     * @param request
     * @return
     */
    @PostMapping(value = "/getFindLoadingWaybillByBranchJson")
    public String getFindLoadingWaybillByBranchJson(Waybill waybill, HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        waybill.setWaybillState("2");
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getFindWaybillByOperateBranchJson(Integer.parseInt(page),Integer.parseInt(rows),waybill,staffInfo);

    }


    /**
     * 装车扫描中修改订单
     * @param waybill
     * @return
     */
    @PostMapping(value = "/getUpdateloadingWaybillById")
    public R getUpdateloadingWaybillById( Waybill waybill,Vehicle vehicle) {

        System.out.println("!!1:"+waybill);

        System.out.println("!!2:"+vehicle);

        return R.ok(waybillService.getUpdateloadingWaybillById(waybill,vehicle));


    }

    /**
     * 卸车扫描中查询所有数据
     * @param waybill
     * @param request
     * @return
     */
    @PostMapping(value = "/getFindUnLoadingWaybillByBranchJson")
    public String getFindUnLoadingWaybillByBranchJson(Waybill waybill, HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        waybill.setWaybillState("5");
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getFindWaybillByOperateBranchJson(Integer.parseInt(page),Integer.parseInt(rows),waybill,staffInfo);

    }


    /**
     * 卸车的修改操作
     * @param waybill
     * @return
     */
    @PostMapping(value = "/getUpdateunloadingWaybillById")
    public R getUpdateunloadingWaybillById( Waybill waybill) {

        System.out.println("!!1:"+waybill);


        return R.ok(waybillService.getUpdateunloadingWaybillById(waybill));


    }


    /**
     * 派件扫描中查询所有数据
     * @param waybill
     * @param request
     * @return
     */
    @PostMapping(value = "/getFindDeliveryWaybillByBranchJson")
    public String getFindDeliveryWaybillByBranchJson(Waybill waybill, HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        waybill.setWaybillState("7");
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getFindWaybillByOperateBranchJson(Integer.parseInt(page),Integer.parseInt(rows),waybill,staffInfo);

    }

    /**
     * 派件扫描中修改
     * @param waybill
     * @return
     */
    @PostMapping(value = "/getUpdateDeliveryWaybillById")
    public R getUpdateDeliveryWaybillById( Waybill waybill) {

        System.out.println("!!1:"+waybill);

        return R.ok(waybillService.getUpdateDeliveryWaybillById(waybill));


    }

    /**
     * 派件扫描中批量修改
     * @param waybill
     * @return
     */
    @PostMapping(value = "/getUpdateAnyDeliveryWaybillById")
    public R getUpdateAnyDeliveryWaybillById( Waybill waybill) {

        System.out.println("!!1:"+waybill);

        return R.ok(waybillService.getUpdateAnyDeliveryWaybillById(waybill));


    }

    /**
     * 到件扫描中查询所有数据
     * @param waybill
     * @param request
     * @return
     */
    @PostMapping(value = "/getFindArrivePieceWaybillByBranchJson")
    public String getFindArrivePieceWaybillByBranchJson(Waybill waybill, HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        waybill.setWaybillState("6");
        System.out.println("Page:" + page + ",rows:" + rows);
        return waybillService.getFindArriveWaybillByOperateBranchJson(Integer.parseInt(page),Integer.parseInt(rows),waybill,staffInfo);

    }

    /**
     * 到件扫描中修改
     * @param waybill
     * @return
     */
    @PostMapping(value = "/getUpdateArrivePieceWaybillById")
    public R getUpdateArrivePieceWaybillById( Waybill waybill, HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        System.out.println("!!1:"+waybill);

        return R.ok(waybillService.getUpdateArrivePieceById(waybill,staffInfo));


    }







}
