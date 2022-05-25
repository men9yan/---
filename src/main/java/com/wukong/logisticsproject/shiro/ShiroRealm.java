package com.wukong.logisticsproject.shiro;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wukong.logisticsproject.mapper.SysRoleMapper;
import com.wukong.logisticsproject.mapper.SysUserRoleMapper;
import com.wukong.logisticsproject.mapper.UserMapper;
import com.wukong.logisticsproject.model.SysRole;
import com.wukong.logisticsproject.model.SysUserRole;
import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.service.impl.SysRoleServiceImpl;
import com.wukong.logisticsproject.service.impl.SysUserRoleServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;


public class ShiroRealm extends AuthorizingRealm {

    @Resource
    private UserMapper userMapper;
    @Resource
    private SysRoleServiceImpl roleService;

    @Resource
    private SysUserRoleServiceImpl userRoleService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("执行授权逻辑");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();

        SysUserRole sysRole = userRoleService.getRoleId(user.getUserId());
        SysRole getRole = roleService.getRole(sysRole.getRoleId());
        System.out.println(getRole.getRoleName());
        info.addRole(getRole.getRoleName());
        System.err.println("角色名："+getRole.getRoleName());
        return info;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationtoken) throws AuthenticationException {

        //编写shiro判断逻辑，判断用户名和密码
        //1.判断用户名
        UsernamePasswordToken Usertoken = (UsernamePasswordToken) authenticationtoken;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", Usertoken.getUsername());

        User dbUser = userMapper.selectOne(queryWrapper);
        System.out.println(dbUser);
        //判断用户是否存在
        if (dbUser != null) {
            if (!Usertoken.getUsername().equals(dbUser.getUsername())) {
                //用户名不存在
                return null;//shiro底层会抛出UnKnowAccountException
            }
            //2.判断密码
            return new SimpleAuthenticationInfo(dbUser, dbUser.getPassword(), this.getClass().getSimpleName());
        }
        return null;
    }


}
