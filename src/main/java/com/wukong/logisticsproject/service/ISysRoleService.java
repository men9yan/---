package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface ISysRoleService extends IService<SysRole> {

    SysRole getRole(Integer roleId);

    List<SysRole> getRoleName();

    List<SysRole> getRoleAll();

    public String getRoleAllJson(@Param("page") Integer page,
                                 @Param("count") Integer count);

    Integer deleteRoleById(Integer roleId);

    Integer updateRoleById(
            @Param("roleId") Integer roleId,
            @Param("roleName") String roleName,
            @Param("createBranch") String createBranch
    );

    Integer getAddRole(
            @Param("roleName") String roleName,
            @Param("createBranch") String createBranch
    );

    List<SysRole> getfindRoleByParam(
            @Param("roleId") Integer roleId,
            @Param("roleName") String roleName,
            @Param("createBranch") String createBranch
    );

    String getRoleJsonByParam(
            @Param("roleId") Integer roleId,
            @Param("roleName") String roleName,
            @Param("createBranch") String createBranch,
            @Param("page") Integer page,
            @Param("count") Integer count);

    Integer addRolePermission(int roleId, String permissionId);
}
