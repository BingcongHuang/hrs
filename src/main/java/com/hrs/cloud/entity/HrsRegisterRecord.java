package com.hrs.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class HrsRegisterRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 创建者
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 用户ID
     */
    @TableField("PATIENT_USER_ID")
    private Long patientUserId;

    /**
     * 医生ID
     */
    @TableField("DOCTOR_USER_ID")
    private Long doctorUserId;

    /**
     * 挂号费用
     */
    @TableField("REGISTER_CHARGES")
    private BigDecimal registerCharges;

    /**
     * 就诊日期
     */
    @TableField("VISITING_DATE")
    private LocalDate visitingDate;

    /**
     * 一级科室ID
     */
    @TableField("FIRST_DPT_ID")
    private Integer firstDptId;

    /**
     * 二级科室ID
     */
    @TableField("SECOND_DPT_ID")
    private Integer secondDptId;

    /**
     * 1:正常挂号,2:追加挂号
     */
    @TableField("SOURCE_TYPE")
    private Integer sourceType;

    /**
     * 状态（0：未使用，1：已使用，2：已取消）
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 1:上午，2：下午
     */
    @TableField("TIME_FLAG")
    private Integer timeFlag;

    /**
     * 删除标识（0：未删除，1：已删除）
     */
    @TableField("DELETED")
    private Integer deleted;

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
