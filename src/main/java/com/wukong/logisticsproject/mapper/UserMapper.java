package com.wukong.logisticsproject.mapper;

import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    Integer resetPasswordById(Integer userId,String password);

    User findUserById(Integer userId);

    List<User> findUserByParam(
            @Param("userId") Integer userId,
            @Param("username") String username,
            @Param("trueName") String trueName
    );

    List<User> selectUserAll();

    Integer deleteUserById(Integer userId);

    Integer updateUserById(User user);

    Integer addUser(
            @Param("username") String username,
            @Param("password") String password,
            @Param("phone") String phone,
            @Param("trueName") String trueName
    );

    List<User> getAllUserByBranch(@Param("list") List<String> usernames);
}
