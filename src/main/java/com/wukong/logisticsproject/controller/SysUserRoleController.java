package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.service.ISysUserRoleService;
import com.wukong.logisticsproject.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户角色表 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Controller
public class SysUserRoleController {

    @Autowired
    ISysUserRoleService service;

    @ResponseBody
    @RequestMapping(value="/showRoleByUser" , method={RequestMethod.POST})
    public R showRoleByUser(HttpServletRequest request){
        Integer userId=Integer.parseInt(request.getParameter("userId"));

        return R.ok(service.getRoleId(userId));
    }

    //更新用户角色信息
    @ResponseBody
    @RequestMapping(value="/updateRoleByUser" , method={RequestMethod.POST})
    public R updateUserIdByRoleId(HttpServletRequest request){
        Integer userId=Integer.parseInt(request.getParameter("updateUserId"));
        Integer roleId=Integer.parseInt(request.getParameter("rol"));

        return R.ok(service.updateUserIdByRoleId(userId,roleId));
    }


}
