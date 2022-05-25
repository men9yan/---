package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 仓库记录表
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_depository_record")
public class DepositoryRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 运单号
     */
    @TableField("waybill_id")
    private String waybillId;

    /**
     * 入库编码
     */
    @TableField("in_code")
    private String inCode;

    /**
     * 入库时间
     */
    @TableField("in_time")
    private LocalDateTime inTime;

    /**
     * 入库仓库
     */
    @TableField("in_depository")
    private String inDepository;

    /**
     * 出库编码
     */
    @TableField("out_code")
    private String outCode;

    /**
     * 出库仓库
     */
    @TableField("out_depository")
    private String outDepository;

    /**
     * 出库时间
     */
    @TableField("out_time")
    private LocalDateTime outTime;

    /**
     * 数量
     */
    @TableField("number")
    private Integer number;


}
