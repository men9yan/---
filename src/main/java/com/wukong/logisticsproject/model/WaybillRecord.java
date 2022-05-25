package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 运单记录表
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_waybill_record")
@Accessors(chain = true)
public class WaybillRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 运单号
     */
    @NotBlank(message = "运单不能为空",groups = VerifySeq.N1.class)
    @TableField("waybill_id")
    private String waybillId;

    /**
     * 操作时间
     */
    @TableField("operate_time")
    private LocalDateTime operateTime;

    @TableField(exist = false)
    private String operateTimeStr;
    /**
     * 运单状态
     */
    @TableField("waybill_state")
    private String waybillState;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 签收人员
     */
    @TableField("signer")
    private String signer;

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
     * 临时信息
     * 下一站网点
     */
    @TableField(exist = false)
    private String nextBranch;

    /**
     * 临时信息
     * 车牌号
     */
    @TableField(exist = false)
    private String vehicleNum;

    /**
     * 临时信息
     * 寄件人
     */
    @TableField(exist = false)
    private String sender;

    /**
     * 临时信息
     * 寄件人电话
     */
    @TableField(exist = false)
    private String senderPhone;

}
