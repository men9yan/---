package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 运单表
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_waybill")
@Accessors(chain = true)
public class Waybill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 运单号
     */
    @TableId("waybill_id")
    private String waybillId;

    /**
     * 多个运单号
     */
    @TableField(exist = false)
    private String waybillIds;

    /**
     * 目的地
     */
    @TableField("destination")
    private String destination;

    /**
     * 目的网点
     */
    @TableField("destination_branch")
    private String destinationBranch;

    /**
     * 收件员
     */
    @TableField("receipt_staff")
    private Integer receiptStaff;

    /**
     * 显示收件员名称
     */
    @TableField(exist = false)
    private String receiptStaffStr;

    /**
     * 寄件时间
     */
    @TableField("send_time")
    private LocalDateTime sendTime;

    /**
     * 运单状态
     * 0:待收件
     * 1:已收件
     * 2:已发件 ？？？
     * 3:已装车
     * 4:已发车
     * 5:已到车 ？？？
     * 6:已卸车 ？？？
     * 7:已到件
     * 8:已派件
     * 9:已签收
     */
    @TableField("waybill_state")
    private String waybillState;

    /**
     * 状态装换成字符串
     */
    @TableField(exist = false)
    private String waybillStateStr;



    /**
     * 签收人员
     */
    @TableField("signer")
    private String signer;

    /**
     * 签收网点
     */
    @TableField("sign_branch")
    private String signBranch;

    /**
     * 签收时间
     */
    @TableField("signer_time")
    private LocalDateTime signerTime;


    /**
     * 寄件网点
     */
    @TableField("send_branch")
    private String sendBranch;

    /**
     * 派件员
     */
    @TableField("dispatch_staff")
    private Integer dispatchStaff;

    /**
     * 显示派件员名称
     */
    @TableField(exist = false)
    private String dispatchStaffStr;

    /**
     * 操作人员
     */
    @TableField("operator")
    private String operator;

    /**
     * 操作网点
     */
    @TableField("operate_branch")
    private String operateBranch;

    /**
     * 操作时间
     */
    @TableField("operate_time")
    private LocalDateTime operateTime;

    @TableField(exist = false)
    private String operateTimeStr;

    /**
     * 添加时间
     */

    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 车牌号
     */
    @TableField("vehicle_num")
    private String vehicleNum;

    /**
     * 司机
     */
    @TableField("vehicle_driver")
    private String vehicleDriver;

    /**
     * 下一站网点
     */
    @TableField("next_branch")
    private String nextBranch;

    /**
     * 货物数量
     */
    @TableField("number")
    private Integer number;

    /**
     * 寄件人
     */
    @NotBlank(message = "发件人姓名不能为空",groups = VerifySeq.N3.class)
    @TableField("sender")
    private String sender;

    /**
     * 发件人电话
     */
    @NotBlank(message = "电话不能为空",groups = VerifySeq.N3.class)
    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$",message = "电话号码不足11位",groups = VerifySeq.N4.class)
    @TableField("sender_phone")
    private String senderPhone;

    /**
     * 收件人
     */
    @NotBlank(message = "收件人姓名不能为空",groups = VerifySeq.N3.class)
    @TableField("consignor")
    private String consignor;


    /**
     * 收件人电话
     */
    @NotBlank(message = "电话不能为空",groups = VerifySeq.N3.class)
    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$",message = "电话号码不足11位",groups = VerifySeq.N5.class)
    @TableField("consignor_phone")
    private String consignorPhone;

    /**
     * 重量
     */
    @TableField("weight")
    private Integer weight;





}
