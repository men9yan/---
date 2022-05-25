package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.ex.ParameterValidationException;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.service.IProblemService;
import com.wukong.logisticsproject.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 问题件表 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@RestController
@Slf4j
public class ProblemController {

    @Autowired
    private IProblemService iProblemService;

    //修改数据
    @ResponseBody
    @RequestMapping(value = "/addProblem", method = {RequestMethod.POST})
    public R addProblem(@Validated(VerifySeq.class) Problem problem, BindingResult bindingResult) {
        System.err.println("接收的数据-->" + problem);

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }

        return R.ok(iProblemService.getAddProblem(problem));

    }

    /**
     * 问题件类型查询
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllProblemTypeJson", method = {RequestMethod.POST})
    public String getAllProblemTypeJson(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);

        return iProblemService.getAllProblemTypeJson(Integer.parseInt(page), Integer.parseInt(rows));
    }

    /**
     * 问题件类型删除
     * @param ptId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDeleteById", method = {RequestMethod.POST})
    public R<User> getDeleteById(Integer ptId) {
        System.err.println("id:" + ptId);
        return R.ok(iProblemService.getDeleteById(ptId));
    }

    /**
     * 问题件类型修改
     * @param problemType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUpdateProblemTypeById", method = {RequestMethod.POST})
    public R getUpdateProblemTypeById(ProblemType problemType) {
        System.out.println(problemType);
        problemType.setPtId(Integer.parseInt(problemType.getPtIdStr()));
        return R.ok(iProblemService.getUpdateProblemTypeById(problemType));
    }

    /**
     * 问题件类型添加
     * @param
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAddProblemType", method = {RequestMethod.POST})
    public R getAddProblemType(HttpServletRequest request) {

        String addptName = request.getParameter("addptName");
        String addptRemark = request.getParameter("addptRemark");
        return R.ok(iProblemService.getAddProblemType(new ProblemType().setPtName(addptName).setPtRemark(addptRemark)));
    }

    /**
     * 查询所有问题件类型
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFindAll", method = {RequestMethod.POST})
    public R getFindAll() {
        return R.ok(iProblemService.getFindAll());
    }

    /**
     * 查询所有问题件信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllProblemJson", method = {RequestMethod.POST})
    public String getAllProblemJson(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);

        return iProblemService.getAllProblemJson(Integer.parseInt(page), Integer.parseInt(rows));
    }

    /**
     * 处理问题件
     * @param problem
     * @param request
     * @return
     */
    @PostMapping(value = "/updateProblem")
    public R updateProblem(Problem problem, HttpServletRequest request) {
        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");

        return R.ok(iProblemService.updateProblem(problem,staffInfo));

    }

    /**
     * 问题件搜索
     * @param waybillId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllProblemByIdJson", method = {RequestMethod.POST})
    public String getAllProblemByIdJson(String waybillId,HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);

        return iProblemService.getAllProblemByIdJson(Integer.parseInt(page), Integer.parseInt(rows),waybillId);
    }










}
