package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 账号
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 电话
     */
    //@NotBlank(message = "电话不能为空",groups = VerifySeq.N3.class)
   // @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$",message = "电话号码格式错误",groups = VerifySeq.N4.class)
    @TableField("phone")
    private String phone;

    /**
     * 头像
     */
    @TableField("img")
    private String img;

    /**
     * 真实姓名
     */
    @TableField("true_name")
    private String trueName;

    /**
     * 是否删除
     */
    @TableField("isdelete")
    private Integer isDelete;

    /**
     * 角色名
     */
    @TableField(exist = false)
    private String roleName;

}
