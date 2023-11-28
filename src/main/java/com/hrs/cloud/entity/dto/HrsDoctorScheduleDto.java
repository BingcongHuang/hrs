package com.hrs.cloud.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrs.cloud.base.BaseDto;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author bingcong huang
 * @since 2023-02-16
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HrsDoctorScheduleDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 医生用户ID
     */
    private Long doctorUserId;

    /**
     * 用户ID````
     */
    private String userName;

    /**
     * 排班类型（1：出诊，2：停诊）
     */
    private Integer scheduleType;

    /**
     * 一级科室ID(医生用)
     */
    private Integer firstDptId;

    /**
     * 二级科室ID(医生用)
     */
    private Integer secondDptId;

    /**
     * 医生级别（1：普通，2：专家）````
     */
    private Integer doctorLevel;

    /**
     * 手机号
     */
    private String userTelphone;

    /**
     * 开始日期
     */
    private String dateStart;

    /**
     * 结束日期
     */
    private String dateEnd;

    /**
     * 周期（1234567代表周一到周日）
     */
    private String daysOfWeek;

    /**
     * 上午班（0：否，1：是）
     */
    private Integer moningFlag;

    /**
     * 下午班（0：否，1：是）
     */
    private Integer afternoonFlag;

}
