package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@Accessors(chain = true)
@TableName("tb_problemType")
public class ProblemType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "PT_id")
    private Integer ptId;

    @TableField(exist = false)
    private String ptIdStr;

    /**
     * 名称
     */
    @TableField("PT_Name")
    private String ptName;

    /**
     * 备注
     */
    @TableField("PT_remark")
    private String ptRemark;

    /**
     * 时间
     */
    @TableField("PT_time")
    private LocalDateTime ptTime;


    /**
     * 软删除
     */
    @TableField("isdelete")
    private String isdelete;

}
