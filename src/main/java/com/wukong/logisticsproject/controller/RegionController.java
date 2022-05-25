package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.model.SysUserRole;
import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.service.ISysUserRoleService;
import com.wukong.logisticsproject.service.IUserService;
import com.wukong.logisticsproject.utils.MD5Utils;
import com.wukong.logisticsproject.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * <p>
 * 地区表 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Controller
public class RegionController {

    @Autowired
    public IUserService userService;

    @Autowired
    public ISysUserRoleService userRoleService;

    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/region")
    public R region(@Valid User user){
        user.setIsDelete(0);
        user.setRoleName("用户");
        user.setPassword(MD5Utils.text2md5(user.getPassword()));
        userService.save(user);
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(2);
        userRoleService.save(userRole);
        return R.ok();
    }
}
