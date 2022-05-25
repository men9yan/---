package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.ex.ParameterValidationException;
import com.wukong.logisticsproject.model.Branch;
import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.VerifySeq;
import com.wukong.logisticsproject.service.IBranchService;
import com.wukong.logisticsproject.service.ICnRegionInfoService;
import com.wukong.logisticsproject.service.IStaffService;
import com.wukong.logisticsproject.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@Slf4j
public class BranchController {
    @Autowired
    private IBranchService branchService;

    @Autowired
    private ICnRegionInfoService regionInfoService;

    @RequestMapping(value = "/getBranchAuditAllList", method = {RequestMethod.POST})
    public String getBranchAuditAllList(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return branchService.getBranchAuditJson(Integer.parseInt(page), Integer.parseInt(rows));
    }

    //搜索数据
    @ResponseBody
    @PostMapping(value = "/findBranchAuditAllById")
    public String findBranchAuditAllById(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        String branchId = request.getParameter("branchId");
        Integer branchId_int;
        if (!branchId.isEmpty()) {
            branchId_int = Integer.parseInt(branchId);
        } else {
            branchId_int = null;
        }
        ;
        return branchService.getBranchAuditAllByIdJson(branchId_int, Integer.parseInt(page), Integer.parseInt(rows));

    }

    @ResponseBody
    @RequestMapping(value = "/showProvincialAll", method = {RequestMethod.POST})
    public R showProvincialAll(HttpServletRequest request) {

        return R.ok(regionInfoService.getfindProvincialAll());
    }

    @ResponseBody
    @RequestMapping(value = "/showCityAll", method = {RequestMethod.POST})
    public R showCityAll(HttpServletRequest request) {
        String criCode=request.getParameter("provincial");
        System.out.println("省级编号："+criCode);

        return R.ok(regionInfoService.getfindCityByProvincial(criCode));
    }

    @ResponseBody
    @RequestMapping(value = "/showDistrictAll", method = {RequestMethod.POST})
    public R showDistrictAll(HttpServletRequest request) {
        String criCode=request.getParameter("city");
        System.out.println("市级编号："+criCode);

        return R.ok(regionInfoService.getfindDistrictByCity(criCode));
    }


    @ResponseBody
    @RequestMapping(value = "/showTownAll", method = {RequestMethod.POST})
    public R showTownAll(HttpServletRequest request) {
        String criCode=request.getParameter("district");
        System.out.println("市级编号："+criCode);

        return R.ok(regionInfoService.getfindTownByDistrict(criCode));
    }








    @ResponseBody
    @RequestMapping(value = "/getAllBranchJson", method = {RequestMethod.POST})
    public String getAllBranchJson(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return branchService.getAllBranchJson(Integer.parseInt(page),Integer.parseInt(rows));
    }


    @ResponseBody
    @RequestMapping(value = "/showBranchByLevel", method = {RequestMethod.POST})
    public R showBranchByLevel(Branch branch) {
        branch.setBranchLevel(Integer.parseInt(branch.getBranchLevel())-1+"");

        return R.ok(branchService.getfindBranchByLevel(branch));
    }


    //添加网点
    @ResponseBody
    @RequestMapping(value = "/addBranch", method = {RequestMethod.POST})
    public R addBranch(@Validated(VerifySeq.class) Branch branch, BindingResult bindingResult) {
        System.err.println("这是什么：" + branch);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }
        return R.ok(branchService.addBranch(branch));
    }


    @ResponseBody
    @RequestMapping(value = "/showAnyBranch", method = {RequestMethod.POST})
    public R showBranch(HttpServletRequest request) {
        String branchId=request.getParameter("branchId");
        System.out.println("网点编号："+branchId);
        return R.ok(branchService.getFindAnyBranch(Integer.parseInt(branchId)));
    }


    /**
     * 修改数据
     * @param branch
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateBranch", method = {RequestMethod.POST})
    public R updateStaff(@Validated(VerifySeq.class) Branch branch, BindingResult bindingResult) {
        System.err.println("接收的数据-->" + branch);

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }

        return R.ok(branchService.getupdateBranchByParam(branch));

    }

    /**
     * 修改网点负责人
     * @param branch
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateBranchHead", method = {RequestMethod.POST})
    public R updateBranchHead( Branch branch) {
        System.err.println("接收的数据-->" + branch);

        return R.ok(branchService.getupdateBranch(branch));

    }




    /**
     * 返回审核前审核情况
     */
    @RequestMapping(value = "/showExamine", method = {RequestMethod.POST})
    public R showExamine() {
        return R.ok(branchService.getExamine());
    }


    /**
     * 通过branch_id获取审核信息
     * @return  updateExamine
     */
    @RequestMapping(value = "/showExamineId", method = {RequestMethod.POST})
    public R showExamineId(HttpServletRequest request) {
        Integer branchId=Integer.parseInt(request.getParameter("branchId"));
        return R.ok(branchService.getExamineById(branchId));
    }

    /**
     * 审核网点功能
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateExamine", method = {RequestMethod.POST})
    public R updateExamine(HttpServletRequest request) {
        Integer branchId=Integer.parseInt(request.getParameter("branchId"));
        String examine = request.getParameter("examine");
        String remark = request.getParameter("remark");
        System.out.println(branchId+"   "+examine+"   sss"+remark);
        return R.ok(branchService.updateExamine(branchId,examine,remark));
    }

    /**
     * 显示已审核的网点
     * @param request
     * @return
     */
    @RequestMapping(value = "/getAllBranchByExamineJson", method = {RequestMethod.POST})
    public String getAllBranchByExamineJson(HttpServletRequest request) {
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        return branchService.getfindAnyBranchByExamineJson(Integer.parseInt(page),Integer.parseInt(rows));
    }



    @ResponseBody
    @RequestMapping(value = "/getFindAnyBranchJson", method = {RequestMethod.POST})
    public String getFindAnyBranchJson(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //

        String branchId=request.getParameter("branchId");

        Integer branchId_int ;
        if(!branchId.isEmpty()){
            branchId_int =  Integer.parseInt(branchId);
        }else{
            branchId_int = null;
        };
        return branchService.getFindAnyBranchJson(branchId_int,Integer.parseInt(page),Integer.parseInt(rows));
    }


    /**
     * 查询所属网点
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/showBranchName", method = {RequestMethod.POST})
    public R showBranchName(HttpServletRequest request) {
        return R.ok(branchService.getfindAnyBranchByExamine());
    }
}
