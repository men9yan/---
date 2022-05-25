package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.ex.ParameterValidationException;
import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.VerifySeq;
import com.wukong.logisticsproject.service.IStaffService;
import com.wukong.logisticsproject.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Controller
@Slf4j
public class StaffController {

    @Autowired
    IStaffService service;

    @RequestMapping(value = "/staffSearchPage", method = {RequestMethod.GET})
    public String staffSearchPage() {
        System.err.println("进来了！！！！！！！！！！！！");
        return "staff/staffSearch";
    }

    @RequestMapping(value = "/staffPage", method = {RequestMethod.GET})
    public String staffpage() {
        System.err.println("进来了！！！！！！！！！！！！");
        return "staff/staff";
    }

    /**
     * 获取所有员工信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllStaffJson", method = {RequestMethod.POST})
    public String list(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return service.getAllStaffJson(Integer.parseInt(page), Integer.parseInt(rows));
    }

    /**
     * 获取所属网点员工信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllStaffJsonByBranch", method = {RequestMethod.POST})
    public String getAllStaffJsonByBranch(HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return service.getAllStaffJsonByBranch(Integer.parseInt(page), Integer.parseInt(rows), staffInfo);
    }


    @ResponseBody
    @RequestMapping(value = "/getAllStaffByDriverJson", method = {RequestMethod.POST})
    public String getAllStaffByDriverJson(HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return service.getAllStaffByDriverJson(Integer.parseInt(page), Integer.parseInt(rows), staffInfo);
    }


    /**
     * 查询所有员工（除负责人和离职的）
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getStaffAllList", method = {RequestMethod.POST})
    public R getStaffAllList() {
        return R.ok(service.getStaffAllList());
    }

    /**
     * 查询所有员工
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getStaffAll", method = {RequestMethod.POST})
    public R getStaffAll() {
        return R.ok(service.getStaffAll());
    }


    //添加用户
    @ResponseBody
    @RequestMapping(value = "/addStaff", method = {RequestMethod.POST})
    public R addStaff(@Validated(VerifySeq.class) Staff staff, BindingResult bindingResult) {
        System.err.println("这是什么：" + staff);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }


        return R.ok(service.getinsertStaff(staff));
    }

    //修改数据
    @ResponseBody
    @RequestMapping(value = "/updateStaff", method = {RequestMethod.POST})
    public R updateStaff(@Validated(VerifySeq.class) Staff staff, BindingResult bindingResult) {
        System.err.println("接收的数据-->" + staff);

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }

        return R.ok(service.updateStaff(staff));

    }

    //删除数据
    @ResponseBody
    @RequestMapping(value = "/deleteStaff", method = {RequestMethod.POST})
    public R<Staff> deleteStaff(Integer StaffId) {
        System.err.println("id:" + StaffId);
        return R.ok(service.deleteStaffById(StaffId));
    }


    //搜索数据
    @ResponseBody
    @RequestMapping(value = "/findStaffByParam", method = {RequestMethod.POST})
    public String findStaffByParam(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        String staffId = request.getParameter("staffid");
        String staffName = request.getParameter("staffname");
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        String branch = staffInfo.getBranch();
        if (staffInfo.getStaffName().equals("admin")) {
            branch = null;
        } else {
            branch = staffInfo.getBranch();
        }
        Integer staffId_int;
        if (!staffId.isEmpty()) {
            staffId_int = Integer.parseInt(staffId);
        } else {
            staffId_int = null;
        }
        ;
        //        System.err.println("staffId:" + Integer.parseInt(staffId) + ",staffName:" + staffName + ",branch:" + branch + ",Page:" + page + ",rows:" + rows);
        return service.getStaffJsonByParam(staffId_int, staffName, branch, Integer.parseInt(page), Integer.parseInt(rows));

    }

    /**
     * 查询本网点所有空闲员工
     *
     * @param branch
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/showStaffByBranch", method = {RequestMethod.POST})
    public R<Staff> showStaffByBranch(String branch) {
        System.err.println("branch:" + branch);
        return R.ok(service.getFindAllByBranchAndRole(branch));
    }


}
