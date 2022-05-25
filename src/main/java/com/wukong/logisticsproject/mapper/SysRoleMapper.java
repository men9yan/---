package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {
    public SysRole getRole(Integer id);

    List<SysRole> findRoleName();

    List<SysRole> findRoleAll();

    Integer deleteById(Integer roleId);

    Integer updateRoleById(
            @Param("roleId") Integer roleId,
            @Param("roleName") String roleName,
            @Param("createBranch") String createBranch
    );

    Integer addRole(
            @Param("roleName") String roleName,
            @Param("createBranch") String createBranch
    );

    List<SysRole> findRoleByParam(
            @Param("roleId") Integer roleId,
            @Param("roleName") String roleName,
            @Param("createBranch") String createBranch
    );
    SysRole findRoleByUser(Integer userId);

    SysRole findNumByName(@Param("roleName") String roleName);
}
