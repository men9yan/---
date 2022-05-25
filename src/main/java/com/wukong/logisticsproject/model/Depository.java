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
 * 仓库表
 * </p>
 *
 * @author wukong.com
 * @since 2020-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_depository")
public class Depository implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 仓库名称
     */
    @TableField("depository_name")
    private String depositoryName;

    /**
     * 仓库地址
     */
    @TableField("address")
    private String address;

    /**
     * 所属网点
     */
    @TableField("branch")
    private String branch;

    /**
     * 仓库负责人
     */
    @TableField("depository_manager")
    private String depositoryManager;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 添加时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 容量
     */
    @TableField("capacity")
    private String capacity;

    /**
     * 温度
     */
    @TableField("temperature")
    private String temperature;

    /**
     * 湿度
     */
    @TableField("humidity")
    private String humidity;


}
