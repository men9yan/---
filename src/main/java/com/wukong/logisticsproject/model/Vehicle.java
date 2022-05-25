package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

/**
 * <p>
 * 车辆信息表
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_vehicle")
@Accessors(chain = true)
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "vehicle_id", type = IdType.AUTO)
    private Integer vehicleId;

    /**
     * 车牌号
     */
    @Pattern(regexp = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}" +
            "[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$"
            ,message = "车牌号格式不对",groups = VerifySeq.N1.class )
    @NotBlank(message = "车牌号不能为空",groups = VerifySeq.N1.class)
    @TableField("vehicle_num")
    private String vehicleNum;

    /**
     * 车辆类型
     */
    @NotBlank(message = "请选择车辆类型",groups = VerifySeq.N2.class)
    @TableField("vehicle_type")
    private String vehicleType;

    /**
     * 时间String类型
     */
    @Past(message = "日期必须在当前日期的过去")
    @NotBlank(message = "时间不能为空",groups = VerifySeq.N3.class)
    @TableField(exist = false)
    private String createTimeStr;

    /**
     * 创建时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 车辆状态
     */
    @NotBlank(message = "车辆状态不能为空",groups = VerifySeq.N5.class)
    @TableField("vehicle_state")
    private String vehicleState;

    /**
     * 车辆状况
     */
    @NotBlank(message = "车辆状况不能为空",groups = VerifySeq.N6.class)
    @TableField("vehicle_status")
    private String vehicleStatus;

    /**
     * 车辆载重
     */
    @Pattern(regexp = "^[0-9]+([.]{1}[0-9]+){0,1}$",message = "请输入数字",groups = VerifySeq.N7.class )
    @DecimalMin(value="0",message="载重必须大于等于0",groups = VerifySeq.N8.class )
    @TableField("vehicle_load")
    private String vehicleLoad;

    /**
     * 车辆净重
     */
    @Pattern(regexp = "^[0-9]+([.]{1}[0-9]+){0,1}$",message = "请输入数字",groups = VerifySeq.N7.class )
    @TableField("vehicle_weight")
    private String vehicleWeight;

    /**
     * 所属网点
     */
    @TableField("branch_suoshu")
    private String branchSuoshu;

    /**
     * 司机
     */
    @TableField("vehicle_driver")
    private String vehicleDriver;

    /**
     * 司机电话
     */
    @TableField("vehicle_phone")
    private Long vehiclePhone;

    /**
     * 员工id
     */
    @TableField("staff_id")
    private Integer staffId;

    /**
     * 车主
     */
    @NotBlank(message = "车主不能为空",groups = VerifySeq.N9.class)
    @TableField("owner")
    private String owner;

    /**
     * 车俩年限
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("vehicle_ageLimit")
    private LocalDateTime vehicleAgeLimit;

    /**
     * 车俩年限String类型
     */
    @NotBlank(message = "时间不能为空",groups = VerifySeq.N10.class)
    @TableField(exist = false)
    private String vehicleAgeLimitStr;


    /**
     * 购车时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("buyCarTime")
    private LocalDateTime buyCarTime;

    /**
     * 购车时间String类型
     */
    @NotBlank(message = "时间不能为空",groups = VerifySeq.N11.class)
    @TableField(exist = false)
    private String buyCarTimeStr;




}
