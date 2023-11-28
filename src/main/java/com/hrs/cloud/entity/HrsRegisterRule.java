package com.hrs.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author bingcong huang
 * @since 2023-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HrsRegisterRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 开始日期
     */
    @TableField("DATE_START")
    private LocalDate dateStart;

    /**
     * 结束日期
     */
    @TableField("DATE_END")
    private LocalDate dateEnd;

    /**
     * 15天内未使用，在线挂号限制数
     */
    @TableField("UNUSED_LIMIT")
    private Integer unusedLimit;

    /**
     * 追加挂号限制数
     */
    @TableField("ADDTION_LIMIT")
    private Integer addtionLimit;

    /**
     * 提前挂号允许的天数
     */
    @TableField("EARLY_DAYS_LIMIT")
    private Integer earlyDaysLimit;

    /**
     * 状态（0：禁用，1：启用）
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 删除标识（0：未删除，1：已删除）
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
