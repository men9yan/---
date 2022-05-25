package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wukong.logisticsproject.ex.DeleteException;
import com.wukong.logisticsproject.ex.InsertException;
import com.wukong.logisticsproject.mapper.StaffMapper;
import com.wukong.logisticsproject.mapper.SysRoleMapper;
import com.wukong.logisticsproject.mapper.UserMapper;
import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.SysRole;
import com.wukong.logisticsproject.model.SysUserRole;
import com.wukong.logisticsproject.mapper.SysUserRoleMapper;
import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 获取角色id
     *
     * @param id
     * @return
     */
    public SysUserRole getRoleId(Integer id) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        return userRoleMapper.selectOne(queryWrapper);
    }

    /**
     * 更新用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    public Integer updateUserIdByRoleId(Integer userId, Integer roleId) {
        QueryWrapper<SysUserRole> sysRoleQueryWrapper = new QueryWrapper<>();
        if (userId==1){
            sysRoleQueryWrapper.eq("user_id", 1);
            SysUserRole roleNameInfo = userRoleMapper.selectOne(sysRoleQueryWrapper);

            if (roleNameInfo!=null) {
                throw new InsertException("超级管理员，不能修改！！");
            }
        }

        Integer rows = userRoleMapper.deleteById(userId);
        Integer rows2 = userRoleMapper.addUserByRole(userId, roleId);
        if (rows2 != 1) {
            throw new InsertException("更新用户角色信息失败！！");
        }
        //修改员工的职位
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_id", userId);
        User user=userMapper.selectOne(userQueryWrapper);
        QueryWrapper<SysRole> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("role_id", roleId);
        SysRole role=sysRoleMapper.selectOne(roleQueryWrapper);
        staffMapper.updateStaffById(new Staff().setPosition(role.getRoleName()).setStaffId(Integer.parseInt(user.getUsername().substring(1))));


        return rows2;
    }
}
