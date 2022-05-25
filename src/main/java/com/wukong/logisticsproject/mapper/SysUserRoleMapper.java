package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    Integer deleteById(Integer userId);

    Integer addUserByRole(@Param("userId") Integer userId,@Param("roleId") Integer roleId);

    SysUserRole findRoleIdByUserId(Integer userId);
}
