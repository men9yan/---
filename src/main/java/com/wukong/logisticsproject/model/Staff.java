package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 *
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_staff")
@Accessors(chain = true)
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工编号
     */
    @TableId(value = "staff_id",type = IdType.AUTO)
    private Integer staffId;

    /**
     * 员工姓名
     */
    @NotBlank(message = "员工姓名不能为空",groups = VerifySeq.N1.class)
    @TableField("staff_name")
    private String staffName;

    /**
     * 性别
     */
    @NotBlank(message = "请选择性别",groups = VerifySeq.N2.class)
    @TableField("gender")
    private String gender;

    /**
     * 电话
     */
    @NotBlank(message = "电话不能为空",groups = VerifySeq.N3.class)
    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$",message = "电话号码格式不正确",groups = VerifySeq.N4.class)
    @TableField("phone")
    private String phone;

    /**
     * 身份证
     *  第一位不可能是0
     *     第二位到第六位可以是0-9
     *     第七位到第十位是年份，所以七八位为19或者20
     *     十一位和十二位是月份，这两位是01-12之间的数值
     *     十三位和十四位是日期，是从01-31之间的数值
     *     十五，十六，十七都是数字0-9
     *     十八位可能是数字0-9，也可能是X
     *
     */
    @Pattern(regexp = "^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|1|2])" +
            "([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$",message = "身份证号码格式错误",groups = VerifySeq.N6.class)
    @NotBlank(message = "身份证号码不能为空",groups = VerifySeq.N5.class)
    @TableField("idCard")
    private String idCard;

    /**
     * 家庭地址
     */
    @TableField("address")
    private String address;

    /**
     * 工作状态
     * 0:空闲
     * 1：忙碌中
     */
    @TableField("state")
    private String state;

    /**
     * 职位
     */
    @NotBlank(message = "职位不能为空",groups = VerifySeq.N8.class)
    @TableField("position")
    private String position;

    /**
     * 所属网点
     */
    @TableField("branch")
    private String branch;

    /**
     * 是否就职
     */
    @NotBlank(message = "请选择是否离职",groups = VerifySeq.N7.class)
    @TableField("isdelete")
    private String isdelete;



}
