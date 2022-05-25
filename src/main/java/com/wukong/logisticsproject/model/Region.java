package com.wukong.logisticsproject.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 地区表
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_region")
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 邮编
     */
    @TableField("postcode")
    private String postcode;

    /**
     * 名字
     */
    @TableField("name")
    private String name;

    /**
     * 省份
     */
    @TableField("province")
    private String province;

    /**
     * 地级市
     */
    @TableField("city")
    private String city;

    /**
     * 县级市
     */
    @TableField("county")
    private String county;

    /**
     * 乡镇
     */
    @TableField("town")
    private String town;

    /**
     * 详细地址
     */
    @TableField("address")
    private String address;


}
