package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.SysRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色权限表 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {
     List<SysRolePermission> getPermissionId(Integer id);
}
