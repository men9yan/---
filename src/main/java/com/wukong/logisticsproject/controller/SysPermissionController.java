package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.mapper.SysPermissionMapper;
import com.wukong.logisticsproject.model.SysPermission;
import com.wukong.logisticsproject.service.ISysPermissionService;
import com.wukong.logisticsproject.service.impl.SysPermissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Controller
public class SysPermissionController {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;


    @Autowired
    private SysPermissionServiceImpl sysPermissionService;



//    @RequestMapping(value = "/menutreedata", method = {RequestMethod.GET})
//    public List<SysPermission> menuTreeData() {
//
//        return sysPermissionMapper.selectList(null);
//    }

    @ResponseBody
    @RequestMapping(value="/menutreedata" , method={RequestMethod.GET})
    public String menuTreeData(){

        return sysPermissionService.menuTreeData();
    }
}
