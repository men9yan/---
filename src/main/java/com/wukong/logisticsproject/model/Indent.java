package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_indent")
@Accessors(chain = true)
public class Indent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */

    @TableId(value = "indent_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long indentId;

    /**
     * 寄件人姓名
     */
    @TableField("sender")
    private String sender;

    /**
     * 寄件人电话
     */
    @TableField("sender_phone")
    private String senderPhone;

    /**
     * 寄件省份
     */
    @TableField("sender_province")
    private String senderProvince;

    /**
     * 寄件地级市
     */
    @TableField("sender_city")
    private String senderCity;

    /**
     * 寄件县级市
     */
    @TableField("sender_county")
    private String senderCounty;

    /**
     * 寄件乡镇
     */
    @TableField("sender_town")
    private String senderTown;

    /**
     * 寄件人住址
     */
    @TableField("sender_address")
    private String senderAddress;

    /**
     * 收件人姓名
     */
    @TableField("consignor")
    private String consignor;

    /**
     * 收件人电话
     */
    @TableField("consignor_phone")
    private String consignorPhone;

    /**
     * 收件省份
     */
    @TableField("consignor_province")
    private String consignorProvince;

    /**
     * 收件地级市
     */
    @TableField("consignor_city")
    private String consignorCity;

    /**
     * 收件县级市
     */
    @TableField("consignor_county")
    private String consignorCounty;

    /**
     * 收件乡镇
     */
    @TableField("consignor_town")
    private String consignorTown;

    /**
     * 收件住址
     */
    @TableField("consignor_address")
    private String consignorAddress;

    /**
     * 当前所在网点
     */
    @TableField("current_branch")
    private String currentBranch;

    /**
     * 规模大小
     */
    @TableField("size")
    private String size;

    /**
     * 状态
     * 0:未分配
     *  1:已分配
     */
    @TableField("state")
    private String state;



    @TableField(exist = false)
    private String stateStr;




    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;

    /**
     * 当前所在仓库
     */
    @TableField("current_depository")
    private String currentDepository;




    @TableField(exist = false)
    private String staffName;


    @TableField(exist = false)
    private Integer staffId;

    @TableField(exist = false)
    private Integer number;

    @TableField(exist = false)
    private Integer weight;


    @TableField(exist = false)
    private String indentIdString;
}
