package com.wukong.logisticsproject.controller;


import com.wukong.logisticsproject.ex.ParameterValidationException;
import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.SysRole;
import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.model.VerifySeq;
import com.wukong.logisticsproject.service.IStaffService;
import com.wukong.logisticsproject.service.ISysRoleService;
import com.wukong.logisticsproject.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Controller
@Slf4j
public class SysRoleController {

    @Autowired
    ISysRoleService service;

    @Autowired
    IStaffService staffService;

    @ResponseBody
    @RequestMapping(value = "/showRole", method = {RequestMethod.POST})
    public R showRole() {
        service.getRoleName().forEach(sysRole -> {
            System.out.println(sysRole);
        });
        return R.ok(service.getRoleName());
    }

    @ResponseBody
    @RequestMapping(value = "/showrolelist", method = {RequestMethod.POST})
    public String list(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        System.out.println("Page:" + page + ",rows:" + rows);

        return service.getRoleAllJson(Integer.parseInt(page), Integer.parseInt(rows));
    }


    //删除数据
    @ResponseBody
    @RequestMapping(value = "/deleteRole", method = {RequestMethod.POST})
    public R<User> deleteRole(Integer roleId) {
        System.err.println("id:" + roleId);
        return R.ok(service.deleteRoleById(roleId));
    }


    //编辑角色信息
    @ResponseBody
    @RequestMapping(value = "/updateRoleById", method = {RequestMethod.POST})
    public R updateUserById(HttpServletRequest request) {
        Integer roleId = Integer.parseInt(request.getParameter("roleId"));
        String roleName = request.getParameter("roleName");
        String createBranch = request.getParameter("createBranch");


        System.err.println("数据获取：" + roleId + "," + roleName + "," + createBranch);
        return R.ok(service.updateRoleById(roleId, roleName, createBranch));
    }


    //添加角色
    @ResponseBody
    @RequestMapping(value = "/addRole", method = {RequestMethod.POST})
    public R addRole(@Validated(VerifySeq.class) SysRole sysRole, BindingResult bindingResult,HttpServletRequest request) {

        Staff staffInfo = (Staff) request.getSession().getAttribute("staffInfo");
        System.err.println("这是什么：" + sysRole);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.debug("验证参数格式失败！{}", errorMessage);
            throw new ParameterValidationException(errorMessage);
        }
        return R.ok(service.getAddRole(sysRole.getRoleName(), staffInfo.getBranch()));
    }


    //搜索数据
    @ResponseBody
    @RequestMapping(value = "/findRoleByParam", method = {RequestMethod.POST})
    public String findUserByParam(HttpServletRequest request) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); //
        String roleId = request.getParameter("roleId");
        String roleName = request.getParameter("roleName");
        String createBranch = request.getParameter("createBranch");
        Integer roleId_int;
        if (!roleId.isEmpty()) {
            roleId_int = Integer.parseInt(roleId);
        } else {
            roleId_int = null;
        }
        ;
        System.err.println("搜索数据：" + page + "," + rows + "," + roleId + "," + roleName + "," + createBranch);
        return service.getRoleJsonByParam(roleId_int, roleName, createBranch, Integer.parseInt(page), Integer.parseInt(rows));

    }

    //分配权限
    @ResponseBody
    @RequestMapping(value = "/addRolePermission", method = {RequestMethod.POST})
    public R addRolePermission(int roleId,String permissionId) {
        String[] permissionArray = permissionId.split(",");
        String permissionStr = "";
        Set<String> treeSet = new TreeSet<String>(Arrays.asList(permissionArray));
        Iterator<String> i = treeSet.iterator();
        while (i.hasNext()) {
            permissionStr += i.next() + ",";
            if (!i.hasNext()) {
                StringBuffer s1 = new StringBuffer(permissionStr);
                s1 = s1.deleteCharAt(s1.length() - 1);
                permissionStr = s1.toString();
            }

        }

        System.out.println("permissionStr:" + permissionStr);
        return R.ok(service.addRolePermission(roleId,permissionStr));
    }
}
