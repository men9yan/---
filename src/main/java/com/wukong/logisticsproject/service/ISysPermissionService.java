package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface ISysPermissionService extends IService<SysPermission> {
    List<SysPermission> getUserMenu(Integer permission_id);

    String menuTreeData();
}
