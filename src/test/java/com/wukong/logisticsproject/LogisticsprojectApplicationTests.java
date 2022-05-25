package com.wukong.logisticsproject;

import com.wukong.logisticsproject.model.Branch;
import com.wukong.logisticsproject.model.SysPermission;
import com.wukong.logisticsproject.model.SysRolePermission;
import com.wukong.logisticsproject.model.SysUserRole;
import com.wukong.logisticsproject.service.*;
import com.wukong.logisticsproject.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
@Slf4j
class LogisticsprojectApplicationTests {
    @Resource
    private ISysRolePermissionService rolePermissionService;

    @Resource
    private ISysUserRoleService userRoleService;

    @Resource
    private ISysPermissionService permissionService;

    @Test
    void contextLoads() {
        SysUserRole sysRole = userRoleService.getRoleId(1);
        System.out.println(sysRole);
        List<SysRolePermission> sysRolePermissions = rolePermissionService.getPermissionId(sysRole.getRoleId());
        for (SysRolePermission sysRolePermission : sysRolePermissions) {
            System.out.println(sysRolePermission.getPermissionId());
            List<SysPermission> allPermissions = permissionService.getUserMenu(sysRolePermission.getPermissionId());
            for (SysPermission allPermission : allPermissions) {
                System.out.println(allPermission);
            }
        }
    }

    @Test
    void MD5() {
        String password = MD5Utils.text2md5("023254");
        System.out.println(password);
    }

    @Autowired
    private IBranchService branchService;


    @Test
    void testcontextLoads() {
        System.out.println(branchService.getExamineById(20200000));
//        List<Branch> branchList = branchService.getExamine();
//        branchList.forEach(branch -> {
//            System.out.println(branch);
//        });
    }

    @Autowired
    private IWaybillService waybillService;
    @Test
    void testWaybillInfo() {
         Double weight=Double.parseDouble("0.001");
        Double vehicleLoad=Double.parseDouble("2");
        System.out.println("净重为："+weight);

        DecimalFormat df2   = new DecimalFormat("0.000");

        String newWeight = df2.format(weight/1000);

    }



    @Test
    void a(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dd=LocalDateTime.parse("2020-11-02 14:42:07",formatter);
        System.out.println(",day:"+dd);
    }


    @Test
    void b(){
        System.out.println(10%3*2);
    }

}
