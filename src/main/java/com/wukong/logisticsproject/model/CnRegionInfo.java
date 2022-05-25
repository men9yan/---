package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("cn_region_info")
public class CnRegionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "CRI_ID", type = IdType.AUTO)
    private Integer criId;

    /**
     * 代码
     */
    @TableField("CRI_CODE")
    private String criCode;

    /**
     * 名称
     */
    @TableField("CRI_NAME")
    private String criName;

    /**
     * 简称
     */
    @TableField("CRI_SHORT_NAME")
    private String criShortName;

    /**
     * 上级代码
     */
    @TableField("CRI_SUPERIOR_CODE")
    private String criSuperiorCode;

    /**
     * 经度
     */
    @TableField("CRI_LNG")
    private String criLng;

    /**
     * 纬度
     */
    @TableField("CRI_LAT")
    private String criLat;

    /**
     * 排序
     */
    @TableField("CRI_SORT")
    private String criSort;

    /**
     * 创建时间
     */
    @TableField("CRI_GMT_CREATE")
    private String criGmtCreate;

    /**
     * 修改时间
     */
    @TableField("CRI_GMT_MODIFIED")
    private String criGmtModified;

    /**
     * 备注
     */
    @TableField("CRI_MEMO")
    private String criMemo;

    /**
     * 状态
     */
    @TableField("CRI_DATA_STATE")
    private LocalDateTime criDataState;

    /**
     * 租户ID
     */
    @TableField("CRI_TENANT_CODE")
    private String criTenantCode;
    /**
     * 级别
     */
    @TableField("CRI_LEVEL")
    private String criLevel;


}
