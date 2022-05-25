package com.wukong.logisticsproject.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.wukong.logisticsproject.ex.ValidationCodeException;
import com.wukong.logisticsproject.model.*;
import com.wukong.logisticsproject.service.*;
import com.wukong.logisticsproject.utils.MD5Utils;
import com.wukong.logisticsproject.vo.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @param
 * @Description: 当前系统用户登录控制器
 */
@Controller
public class LoginController {
    @Resource
    private ISysRolePermissionService rolePermissionService;

    @Resource
    private ISysUserRoleService userRoleService;

    @Resource
    private ISysRoleService roleService;

    @Resource
    private ISysPermissionService permissionService;

    @Autowired
    private Producer producer;

    @Autowired
    private IStaffService staffService;


    /**
     * 主页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "backoffice")
    public String backoffice(HttpServletRequest request) {
        try {
            List<SysPermission> list = new ArrayList<>();
            User loginUser = (User) request.getSession().getAttribute("loginUser");
            SysUserRole sysRole = userRoleService.getRoleId(loginUser.getUserId());
            SysRole role = roleService.getRole(sysRole.getRoleId());
            List<SysRolePermission> sysRolePermissions = rolePermissionService.getPermissionId(sysRole.getRoleId());
            for (SysRolePermission sysRolePermission : sysRolePermissions) {
                List<SysPermission> allPermissions = permissionService.getUserMenu(sysRolePermission.getPermissionId());
                for (SysPermission allPermission : allPermissions) {
                    System.out.println(allPermission);
                    list.add(allPermission);
                }
            }
            loginUser.setRoleName(role.getRoleName());
            request.setAttribute("menulist", list);
            request.setAttribute("loginUser", loginUser);
            request.getSession().setAttribute("roleName", role.getRoleName());
            if (loginUser.getUsername().equals("admin")) {
                Staff AdminInfo = new Staff();
                AdminInfo.setBranch("总公司");
                AdminInfo.setStaffName("admin");
                AdminInfo.setPhone(loginUser.getPhone());
                request.getSession().setAttribute("staffInfo", AdminInfo);
            } else {
                String staffId = loginUser.getUsername().substring(1);
                Staff staffInfo = staffService.getReceiptBaseInfo(staffId);
                request.getSession().setAttribute("staffInfo", staffInfo);
            }
        } catch (Exception e) {
            return "redirect:index";
        }
        System.out.println(request.getSession().getAttribute("staffInfo"));
        return "backoffice";
    }

    /**
     * 登录功能
     *
     * @param user
     * @param imageCode
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/login")
    public R login(@Valid User user, @RequestParam("imageCode") String imageCode, HttpSession session) {
        /**
         * 使用Shiro编写认证操作
         */
        //1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        String verifyCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        System.out.println(imageCode + "====" + verifyCode);
        if (!imageCode.equals(verifyCode) || imageCode.isEmpty()) {
            throw new ValidationCodeException("验证码错误");

        }
        //2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), MD5Utils.text2md5(user.getPassword()));
        System.out.println(user.getUsername() + " " + user.getPassword());
        //3.执行登录方法
        try {
            subject.login(token);//登陆成功
            User loginUser = (User) subject.getPrincipal();
            System.err.println("角色+？？？："+subject.hasRole("superadmin"));
            subject.getSession().setAttribute("loginUser", loginUser);

            //跳转到主页面
            return R.ok();
        } catch (UnknownAccountException e) {
            //e.printStackTrace();
            //登录失败：用户名不存在
            throw new ValidationCodeException("用户名不存在");

        } catch (IncorrectCredentialsException e) {
            //e.printStackTrace();
            //登录失败：密码错误
            throw new ValidationCodeException("密码错误");
        }
    }


    /**
     * 获取验证码图片
     *
     * @param response
     * @param request
     */
    @ResponseBody
    @GetMapping("/getVerifyCode")
    public void captcha(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        // 生成文字验证码
        String text = producer.createText();
        // 生成图片验证码
        BufferedImage image = producer.createImage(text);
        // 保存验证码到session
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }


    /**
     * 安全退出
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/logout")
    public String logout() throws Exception {
        SecurityUtils.getSubject().logout();
        return "redirect:/index";
    }
}
