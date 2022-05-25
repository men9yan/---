package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.ex.ParameterValidationException;
import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.model.Vehicle;
import com.wukong.logisticsproject.model.VerifySeq;
import com.wukong.logisticsproject.service.IVehicleService;
import com.wukong.logisticsproject.utils.DateUtils;
import com.wukong.logisticsproject.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * <p>
 * 车辆信息表 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Controller
@Slf4j
public class VehicleController {
    @Autowired
    IVehicleService vehicleService;

    @RequestMapping(value = "/vehiclePage", method = {RequestMethod.GET})
    public String vehiclePage() {
        System.err.println("进来了！！！！！！！！！！！！");
        return "vehicle/vehicle";
    }


    /**
     * 获取所有车辆信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVehicleAllList", method = {RequestMethod.POST})
    public String getVehicleAllList(HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        System.err.println("staffInfo==>"+staffInfo);
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return vehicleService.getAllVehicleJson(Integer.parseInt(page), Integer.parseInt(rows),staffInfo);
    }


    /**
     * 查询空闲车辆信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getVehicleByParamJson", method = {RequestMethod.POST})
    public String getVehicleByParamJson(HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        System.err.println("进来了！！！！！！！！！！！！");
        String nextBranch=request.getParameter("nextBranch");
        System.out.println("nextBranch:" + nextBranch);
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return vehicleService.getVehicleByParamJson(Integer.parseInt(page), Integer.parseInt(rows),staffInfo,nextBranch);
    }





    /**
     * 添加车辆
     * @param vehicle
     * @param bindingResult
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @PostMapping(value = "/addVehicle")
    public R addVehicle(@Validated(VerifySeq.class) Vehicle vehicle, BindingResult bindingResult) throws ParseException {
        System.err.println("这是什么：" + vehicle);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }

        Date date = DateUtils.string2Date(vehicle.getCreateTimeStr());
        vehicle.setCreateTime(DateUtils.date2String(date));
        vehicle.setVehicleAgeLimit(DateUtils.date2String(DateUtils.string2Date(vehicle.getVehicleAgeLimitStr())));
        vehicle.setBuyCarTime(DateUtils.date2String(DateUtils.string2Date(vehicle.getBuyCarTimeStr())));
        return R.ok(vehicleService.getInsertVehicle(vehicle));
    }

    //删除数据
    @ResponseBody
    @RequestMapping(value = "/deleteVehicle", method = {RequestMethod.POST})
    public R<Vehicle> deleteVehicle(Integer vehicleId) {
        System.err.println("vehicleId:" + vehicleId);
        return R.ok(vehicleService.deleteVehicleById(vehicleId));
    }

    //修改数据
    @ResponseBody
    @RequestMapping(value = "/updateVehicle", method = {RequestMethod.POST})
    public R updateVehicle(@Validated(VerifySeq.class) Vehicle vehicle, BindingResult bindingResult) throws ParseException {
        System.err.println("接收的数据-->" + vehicle);

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }
        Date date = DateUtils.string2Date(vehicle.getCreateTimeStr());
        vehicle.setCreateTime(DateUtils.date2String(date));
        vehicle.setVehicleAgeLimit(DateUtils.date2String(DateUtils.string2Date(vehicle.getVehicleAgeLimitStr())));
        vehicle.setBuyCarTime(DateUtils.date2String(DateUtils.string2Date(vehicle.getBuyCarTimeStr())));

        return R.ok(vehicleService.updateVehicle(vehicle));

    }

    //搜索数据
    @ResponseBody
    @RequestMapping(value = "/findVehicleByParam", method = {RequestMethod.POST})
    public String findVehicleByParam(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        String vehicleNum = request.getParameter("vehicleNum");
        String vehicleDriver = request.getParameter("vehicleDriver");
        String vehicleState = request.getParameter("vehicleState");
        System.out.println(vehicleNum+"---"+vehicleDriver+"---"+vehicleState);
        return vehicleService.getVehicleJsonByParam(vehicleNum, vehicleDriver, vehicleState,  Integer.parseInt(page), Integer.parseInt(rows));
    }

    /**
     * 装车扫描中查询车辆数据
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findVehicleSearchByParam", method = {RequestMethod.POST})
    public String findVehicleSearchByParam(HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        System.err.println("进来了！！！！！！！！！！！！");
        String nextBranch=request.getParameter("nextBranch");
        System.out.println("nextBranch:" + nextBranch);
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        String vehicleId = request.getParameter("vehicleId");
        Integer vehicleId_int ;
        if(!vehicleId.isEmpty()){
            vehicleId_int =  Integer.parseInt(vehicleId);
        }else{
            vehicleId_int = null;
        };
        System.out.println(vehicleId);
        return vehicleService.getVehicleSearchByParamJson(Integer.parseInt(page), Integer.parseInt(rows),staffInfo,nextBranch,vehicleId_int);
    }




}
