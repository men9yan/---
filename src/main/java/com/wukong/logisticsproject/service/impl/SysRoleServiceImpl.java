package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wukong.logisticsproject.ex.DeleteException;
import com.wukong.logisticsproject.ex.InsertException;
import com.wukong.logisticsproject.ex.UpdateException;
import com.wukong.logisticsproject.mapper.SysRolePermissionMapper;
import com.wukong.logisticsproject.mapper.SysUserRoleMapper;
import com.wukong.logisticsproject.mapper.UserMapper;
import com.wukong.logisticsproject.model.SysRole;
import com.wukong.logisticsproject.mapper.SysRoleMapper;
import com.wukong.logisticsproject.model.SysRolePermission;
import com.wukong.logisticsproject.model.SysUserRole;
import com.wukong.logisticsproject.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wukong.logisticsproject.utils.JqgridUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;

    @Autowired
    private JqgridUtil jqgridUtil;

    public SysRole getRole(Integer id) {
        return roleMapper.getRole(id);
    }

    @Override
    public List<SysRole> getRoleName() {
        List<SysRole> roles = roleMapper.findRoleName();
        return roles;
    }

    @Override
    public List<SysRole> getRoleAll() {
        return roleMapper.findRoleAll();
    }

    @Override
    public String getRoleAllJson(Integer page, Integer count) {
        List<SysRole> roles = getRoleAll();
        int toIndex = count * page;
        if (roles.size() < toIndex) {
            toIndex = roles.size();
        }
        List<SysRole> list = roles.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", roles.size(), count);
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @Override
    public Integer deleteRoleById(Integer roleId) {
        QueryWrapper<SysRole> roleQueryWrapper = new QueryWrapper();
        roleQueryWrapper.eq("role_id", roleId);
        SysRole sysRole = roleMapper.selectOne(roleQueryWrapper);
        if (sysRole.getRoleName().equals("superadmin")) {
            throw new DeleteException("超级管理员不能删除！！");
        }
        Integer rows = roleMapper.deleteById(roleId);
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_Id", roleId);
        sysUserRoleMapper.delete(queryWrapper);
        if (rows != 1) {
            throw new UpdateException("删除角色失败！！！");
        }
        return rows;
    }

    /**
     * 修改角色信息
     *
     * @param roleId
     * @param roleName
     * @param createBranch
     * @return
     */
    @Override
    public Integer updateRoleById(Integer roleId, String roleName, String createBranch) {
        QueryWrapper<SysRole> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("role_name", roleName);
        SysRole roleInfo = roleMapper.selectOne(roleQueryWrapper);
        if (roleInfo != null) {
            throw new InsertException("角色已经存在！！");
        }
        QueryWrapper<SysRole> sysRoleQueryWrapper = new QueryWrapper<>();
        sysRoleQueryWrapper.eq("role_id", roleId);
        SysRole sysRole = roleMapper.selectOne(sysRoleQueryWrapper);
        if (sysRole.getRoleName().equals("superadmin")) {
            throw new DeleteException("超级管理员不能修改！！");
        }
        Integer row = roleMapper.updateRoleById(roleId, roleName, createBranch);
        if (row != 1) {
            throw new UpdateException("修改角色失败！！！");
        }
        return row;
    }


    @Override
    public Integer getAddRole(String roleName, String createBranch) {
        QueryWrapper<SysRole> roleQueryWrapper = new QueryWrapper<>();

        roleQueryWrapper.eq("role_name", roleName);
        SysRole roleInfo = roleMapper.selectOne(roleQueryWrapper);
        if (roleInfo != null) {
            roleQueryWrapper.eq("isdelete", 1);
            SysRole sysRoleInfo = roleMapper.selectOne(roleQueryWrapper);
            if (sysRoleInfo != null) {
                roleInfo.setIdDelete(0);
                Integer row = roleMapper.updateById(roleInfo);
                if (row != 1) {
                    throw new InsertException("插入数据失败！！");
                }
                return row;
            } else {
                throw new InsertException("角色已经存在！！");

            }
        }
        Integer row = roleMapper.addRole(roleName, createBranch);
        if (row != 1) {
            throw new InsertException("插入数据失败！！");
        }
        return row;
    }

    @Override
    public List<SysRole> getfindRoleByParam(Integer roleId, String roleName, String createBranch) {
        List<SysRole> sysRoles = roleMapper.findRoleByParam(roleId, roleName, createBranch);
        return sysRoles;
    }

    @Override
    public String getRoleJsonByParam(Integer roleId, String roleName, String createBranch, Integer page, Integer count) {
        List<SysRole> sysRoles = getfindRoleByParam(roleId, roleName, createBranch);
        int toIndex = count * page;
        if (sysRoles.size() < toIndex) {
            toIndex = sysRoles.size();
        }
        List<SysRole> list = sysRoles.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", sysRoles.size(), count);
    }

    /**
     * 添加权限
     *
     * @param roleId
     * @param permissionId
     * @return
     */
    @Override
    public Integer addRolePermission(int roleId, String permissionId) {
        List<String> permissionIdList = null;
        SysRolePermission rolePermission = new SysRolePermission();
        QueryWrapper<SysRolePermission> queryWrapper = new QueryWrapper();
        QueryWrapper<SysRole> roleQueryWrapper = new QueryWrapper();
        int rows = 0;

        roleQueryWrapper.eq("role_id", roleId);
        SysRole sysRole = roleMapper.selectOne(roleQueryWrapper);

        if (sysRole.getRoleName().equals("superadmin")) {
            throw new DeleteException("超级管理员不能修改！！");
        }

        if (permissionId.contains(",")) {
            permissionIdList = Arrays.asList(permissionId.split(","));
        } else {
            permissionIdList = new ArrayList<String>();
            permissionIdList.add(permissionId);
        }

        queryWrapper.eq("role_id", roleId);
        rolePermissionMapper.delete(queryWrapper);
        for (String permissionIds : permissionIdList) {
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(Integer.parseInt(permissionIds));
            rows = rolePermissionMapper.insert(rolePermission);
        }
        if (rows != 1) {
            throw new InsertException("插入数据失败！！");
        }
        return rows;
    }


}
