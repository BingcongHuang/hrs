package com.hrs.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 门诊科室（二级）
 * </p>
 *
 * @author bingcong huang
 * @since 2023-02-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HrsDepartmentSecond implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 科室号
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 一级科室号
     */
    @TableField("FDPT_ID")
    private Integer fdptId;

    /**
     * 科室名称
     */
    @TableField("DPT_CNAME")
    private String dptCname;

    /**
     * 科室英文名称
     */
    @TableField("DPT_ENAME")
    private String dptEname;

    /**
     * 联系方式
     */
    @TableField("DPT_PHONE")
    private String dptPhone;

    /**
     * 科室简介
     */
    @TableField("DPT_REMARK")
    private String dptRemark;

    /**
     * 地址
     */
    @TableField("ADDRESS")
    private String address;

    /**
     * 顺序
     */
    @TableField("SORT_NUM")
    private Integer sortNum;

    /**
     * 状态（0：禁用，1：启用）
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 删除（0：未删除，1：已删除）
     */
    @TableField("DELETED")
    private Integer deleted;

    /**
     * 创建者
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;


}
