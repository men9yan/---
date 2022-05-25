package com.wukong.logisticsproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wukong.logisticsproject.model.SysPermission;
import com.wukong.logisticsproject.mapper.SysPermissionMapper;
import com.wukong.logisticsproject.model.TreeNode;
import com.wukong.logisticsproject.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Autowired
    private SysPermissionMapper permissionMapper;
    @Autowired
    private Gson gson;

    /**
     * 获取用户菜单
     *
     * @param permission_id
     * @return
     */
    public List<SysPermission> getUserMenu(Integer permission_id) {
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("permission_id", permission_id);
        return permissionMapper.selectList(queryWrapper);
    }

    @Override
    public String menuTreeData() {
        List<TreeNode> result = new ArrayList<TreeNode>();
        List<SysPermission> menuList = permissionMapper.selectList(new QueryWrapper<SysPermission>().isNull("pid"));
        List<SysPermission> menuChildList = permissionMapper.selectList(new QueryWrapper<SysPermission>().isNotNull("pid"));
        for (SysPermission menulist : menuList) {
            List<TreeNode> childNode = new ArrayList<TreeNode>();
            TreeNode node = new TreeNode();
            node.setId(menulist.getPermissionId());
            node.setText(menulist.getPermissionName());
            node.setState("closed");

            menuChildList.forEach(menuChildlist -> {

                if (menuChildlist.getPid().equals(menulist.getPermissionId())) {
                    TreeNode child = new TreeNode();
                    child.setId(menuChildlist.getPermissionId());
                    child.setText(menuChildlist.getPermissionName());
                    child.setState(null);
                    childNode.add(child);
                }

            });
            TreeNode child = new TreeNode();
            child.setId(menulist.getPermissionId());
            child.setText(menulist.getPermissionName());
            child.setState(null);
            childNode.add(child);
            node.setChildren(childNode);
            result.add(node);

        }
        return gson.toJson(result);
    }

}
