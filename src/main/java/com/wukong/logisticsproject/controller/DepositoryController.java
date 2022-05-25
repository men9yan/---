package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.model.Depository;
import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.service.IDepositoryService;
import com.wukong.logisticsproject.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 仓库表 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@RestController
public class DepositoryController {

    @Autowired
    private IDepositoryService depositoryService;

    @PostMapping(value = "/getDepository")
    public String getDepository(HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);
        return depositoryService.getDepository(Integer.parseInt(page),Integer.parseInt(rows),loginUser);
    }

    @PostMapping(value = "/updateData")
    public R getDepository(Depository depository){
        System.out.println("收到的数据：" + depository);

        return R.ok(depositoryService.addData(depository));
    }



}
