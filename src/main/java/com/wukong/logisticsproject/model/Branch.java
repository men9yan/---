package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
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
@NoArgsConstructor
@TableName("tb_branch")
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 网点编号
     */
    @TableId(value = "branch_id", type = IdType.AUTO)
    private Integer branchId;

    /**
     * 网点名称
     */
    @NotBlank(message = "网点名称不能为空",groups = VerifySeq.N2.class)
    @TableField("branch_name")
    private String branchName;

    /**
     * 省份
     */
    @NotBlank(message = "省份不能为空",groups = VerifySeq.N1.class)
    @TableField("branch_province")
    private String branchProvince;

    /**
     * 地级市
     */
    @TableField("branch_city")
    private String branchCity;

    /**
     * 县级市
     */
    @TableField("branch_county")
    private String branchCounty;

    /**
     * 乡镇
     */
    @TableField("branch_town")
    private String branchTown;

    /**
     * 网点地址
     */
    @NotBlank(message = "地址不能为空",groups = VerifySeq.N4.class)
    @TableField("branch_address")
    private String branchAddress;

    /**
     * 审核情况
     * 1:待审核
     * 2:审核成功
     * 3:审核失败
     */
    @TableField("examine")
    private String examine;


    @TableField(exist = false)
    private String examineStr;


    /**
     * 网点负责人
     */
    @TableField("branch_manager")
    private String branchManager;

    /**
     * 网点电话
     */
    @NotBlank(message = "电话不能为空",groups = VerifySeq.N3.class)
    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$",message = "电话格式输入有误",groups = VerifySeq.N5.class)
    @TableField("branch_phone")
    private String branchPhone;

    /**
     * 网点等级
     */
    @NotBlank(message = "网点等级不能为空",groups = VerifySeq.N6.class)
    @TableField("branch_level")
    private String branchLevel;

    /**
     * 网点建立时间
     */
    @TableField("branch_time")
    private LocalDateTime branchTime;

    /**
     * 所属网点
     */
    @TableField("suosu_branch")
    private String suosuBranch;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 负责人id
     */
    @TableField("staff_id")
    private String staffId;

    public Branch(String examine, String examineStr) {
        this.examine = examine;
        this.examineStr = examineStr;
    }
}
