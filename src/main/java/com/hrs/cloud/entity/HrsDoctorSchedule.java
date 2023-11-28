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
 * @since 2023-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HrsDoctorSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 排班类型（1：出诊，2：停诊）
     */
    @TableField("SCHEDULE_TYPE")
    private Integer scheduleType;

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
     * 周期（1234567代表周一到周日）
     */
    @TableField("DAYS_OF_WEEK")
    private String daysOfWeek;

    /**
     * 上午班（0：否，1：是）
     */
    @TableField("MONING_FLAG")
    private Integer moningFlag;

    /**
     * 下午班（0：否，1：是）
     */
    @TableField("AFTERNOON_FLAG")
    private Integer afternoonFlag;

    /**
     * 上午限制数
     */
    @TableField("MONING_LIMIT")
    private Integer moningLimit;

    /**
     * 下午限制数
     */
    @TableField("AFTERNOON_LIMIT")
    private Integer afternoonLimit;

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
