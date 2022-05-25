package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wukong.logisticsproject.mapper.SysRoleMapper;
import com.wukong.logisticsproject.mapper.SysUserRoleMapper;
import com.wukong.logisticsproject.model.SysRole;
import com.wukong.logisticsproject.model.SysRolePermission;
import com.wukong.logisticsproject.mapper.SysRolePermissionMapper;
import com.wukong.logisticsproject.model.SysUserRole;
import com.wukong.logisticsproject.service.ISysRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {
    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;

    /**
     * 获取权限Id
     * @param id
     * @return
     */
    public List<SysRolePermission> getPermissionId(Integer id){
        QueryWrapper<SysRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",id);
        return rolePermissionMapper.selectList(queryWrapper);
    }
}
