package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 问题件表
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_problem")
@Accessors(chain = true)
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */

    @TableId(value = "problem_id",type = IdType.AUTO)
    private Integer problemId;

    /**
     * 运单编号
     */
    @TableField("waybill_id")
    private String waybillId;

    /**
     * 问题描述
     */
    @TableField("problem_desc")
    private String problemDesc;

    /**
     * 接收网点
     */
    @TableField("receive_branch")
    private String receiveBranch;

    /**
     * 责任方
     */
    @TableField("responsible_party")
    private String responsibleParty;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 登记网点
     */
    @TableField("register_branch")
    private String registerBranch;

    /**
     * 登记人
     */
    @TableField("registrant")
    private String registrant;

    /**
     * 登记时间
     */
    @TableField("register_time")
    private LocalDateTime registerTime;

    /**
     * 处理网点
     */
    @TableField("handle_branch")
    private String handleBranch;

    /**
     * 处理人
     */
    @TableField("handler")
    private String handler;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 处理时间
     */
    @TableField("handle_time")
    private LocalDateTime handleTime;

    /**
     * 问题件类型
     */
    @TableField("ptName")
    private String ptName;


}
