package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wukong.logisticsproject.ex.DeleteException;
import com.wukong.logisticsproject.ex.ResetPasswordException;
import com.wukong.logisticsproject.ex.UpdateException;
import com.wukong.logisticsproject.mapper.*;
import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.SysRole;
import com.wukong.logisticsproject.model.SysUserRole;
import com.wukong.logisticsproject.model.User;
import com.wukong.logisticsproject.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wukong.logisticsproject.utils.JqgridUtil;
import com.wukong.logisticsproject.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private JqgridUtil jqgridUtil;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private BranchMapper branchMapper;


    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public List<User> getUserAllList() {
        return userMapper.selectUserAll();
    }

    /**
     * 获取全部User数据的Json格式
     *
     * @param page
     * @param count
     * @return
     */
    @Override
    public String getAllUserJson(Integer page, Integer count) {
        List<User> user = getUserAllList();
        user.forEach(user1 -> {
            System.out.println(user);
        });
        for (int i = 0; i < user.size(); i++) {
            SysRole role = (sysRoleMapper.findRoleByUser(user.get(i).getUserId()));
            String roleName = null;
            if (role != null) {
                roleName = role.getRoleName();
            }
            user.get(i).setRoleName(roleName);
        }
        user.forEach(user1 -> {
            System.out.println(user1);
        });
        int toIndex = count * page;
        if (user.size() < toIndex) {
            toIndex = user.size();
        }
        List<User> list = user.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", user.size(), count);
    }

    @Override
    public String getAllUserByBranchJson(Integer page, Integer count,Staff staffInfo) {

        List<User> user = null;
        if(staffInfo.getStaffName().equals("admin")){
            user = getUserAllList();
        }else{
            QueryWrapper<Staff> staffQueryWrapper = new QueryWrapper<>();
            staffQueryWrapper.eq("branch", staffInfo.getBranch());
            List<Staff> staffs = staffMapper.selectList(staffQueryWrapper);


            ArrayList<String> usernames = new ArrayList<String>();

            for (Staff staff : staffs) {
                String name="Y"+staff.getStaffId();
                System.out.println("遍历的内容为："+name);
                usernames.add(name);
            }
            System.out.println("数据为："+usernames);

            user=userMapper.getAllUserByBranch(usernames);
        }



        for (int i = 0; i < user.size(); i++) {
            SysRole role = (sysRoleMapper.findRoleByUser(user.get(i).getUserId()));
            String roleName = null;
            if (role != null) {
                roleName = role.getRoleName();
            }
            user.get(i).setRoleName(roleName);
        }
        user.forEach(user1 -> {
            System.out.println(user1);
        });
        int toIndex = count * page;
        if (user.size() < toIndex) {
            toIndex = user.size();
        }
        List<User> list = user.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", user.size(), count);
    }


    /**
     * 修改密码
     *
     * @param userId
     * @param oldpassword
     * @param password
     * @return
     */
    @Override
    public Integer updatePassword(Integer userId, String oldpassword, String password) {

        User user = userMapper.findUserById(userId);
        if (!user.getPassword().equals(MD5Utils.text2md5(oldpassword))) {
            throw new ResetPasswordException("原密码错误！！");
        }
        String newpassword = MD5Utils.text2md5(password);
        Integer rows = userMapper.resetPasswordById(userId, newpassword);
        if (rows != 1) {
            throw new UpdateException("重置密码失败！！");
        }

        return rows;
    }

    @Override
    public List<User> getUserByParam(Integer userId, String username, String trueName) {
        List<User> userList = userMapper.findUserByParam(userId, username, trueName);
        return userList;
    }

    /**
     * 通过参数查询数据
     *
     * @param page
     * @param count
     * @param userId
     * @param username
     * @param trueName
     * @return
     */
    @Override
    public String getUserJsonByParam(Integer page, Integer count, Integer userId, String username, String trueName) {
        List<User> userList = getUserByParam(userId, username, trueName);
        for (int i = 0; i < userList.size(); i++) {
            SysRole role = (sysRoleMapper.findRoleByUser(userList.get(i).getUserId()));
            String roleName = null;
            if (role != null) {
                roleName = role.getRoleName();
            }
            userList.get(i).setRoleName(roleName);
        }
        int toIndex = count * page;
        if (userList.size() < toIndex) {
            toIndex = userList.size();
        }
        List<User> list = userList.subList(count * (page - 1), toIndex);
        return jqgridUtil.getJson(list, page + "", userList.size(), count);
    }


    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @Override
    public Integer deleteUserByid(Integer userId,String username) {
        QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", userId);
        SysUserRole userRoleInfo = sysUserRoleMapper.selectOne(userRoleQueryWrapper);
        if(userRoleInfo!=null){

            QueryWrapper<SysRole> roleQueryWrapper = new QueryWrapper<>();
            roleQueryWrapper.eq("role_id", userRoleInfo.getRoleId());
            SysRole roleInfo = sysRoleMapper.selectOne(roleQueryWrapper);

            if (roleInfo.getRoleName().equals("superadmin")) {
                throw new DeleteException("超级管理员不能删除！！");
            }
            if (roleInfo.getRoleName().contains("网点负责人")) {
                throw new DeleteException("网点负责人不能删除！！");
            }

        }
        //修改员工是否离职改成是
        staffMapper.updateStaffById(new Staff().setIsdelete("是").setStaffId(Integer.parseInt(username.substring(1))));

        branchMapper.updateNullBranch(Integer.parseInt(username.substring(1)));

        Integer row = userMapper.deleteUserById(userId);
        if (row != 1) {
            throw new DeleteException("删除用户失败！！！");
        }
        return row;
    }

    @Override
    public Integer getUpdateUserById(User user, HttpServletRequest request) {

        Integer rows = userMapper.updateUserById(user);
        if (rows != 1) {
            throw new UpdateException("修改数据失败！！！");
        }
        //更新用户信息
        User loginUser = userMapper.findUserById(user.getUserId());
        request.getSession().setAttribute("loginUser", loginUser);
        return rows;
    }

    /**
     * 重置密码
     *
     * @param userId
     * @return
     */
    @Override
    public Integer resetPassword(Integer userId) {
        User userInfo = userMapper.selectById(userId);
        String userPhone = userInfo.getPhone();
        userInfo.setPassword(MD5Utils.text2md5(userPhone.substring(userPhone.length() - 6, userPhone.length())));
        int rows = userMapper.updateById(userInfo);
        if (rows != 1) {
            throw new UpdateException("修改数据失败！！！");
        }
        return rows;
    }


}
