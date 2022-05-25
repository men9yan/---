package com.wukong.logisticsproject.service;

import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
public interface IUserService extends IService<User> {

    List<User> getUserAllList();


    public String getAllUserJson(@Param("page") Integer page,
                                  @Param("count") Integer count);

    public String getAllUserByBranchJson(@Param("page") Integer page,
                                 @Param("count") Integer count,@Param("staffInfo") Staff staffInfo);

    Integer updatePassword(@Param("userId") Integer userId,
                        @Param("oldpassword") String oldpassword,
                       @Param("password") String password);

    List<User> getUserByParam(
            @Param("userId") Integer userId,
            @Param("username") String username,
            @Param("trueName") String trueName
    );

    public String getUserJsonByParam(
            @Param("page") Integer page,
            @Param("count") Integer count,
            @Param("userId") Integer userId,
            @Param("username") String username,
            @Param("trueName") String trueName
    );

    Integer deleteUserByid(Integer userId,String username);

    Integer getUpdateUserById(  User user , HttpServletRequest request);

    Integer resetPassword(Integer userId);

}
