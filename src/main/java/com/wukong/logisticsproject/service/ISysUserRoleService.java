package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface ISysUserRoleService extends IService<SysUserRole> {
    SysUserRole getRoleId(Integer id);

    //更新用户的角色
    Integer updateUserIdByRoleId(Integer userId,Integer roleId);
}
