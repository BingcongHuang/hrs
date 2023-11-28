package com.hrs.cloud.entity.dto;

import com.hrs.cloud.base.BaseDto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author bingcong huang
 * @since 2023-02-16
 */
@Data
public class HrsRegisterDoctorDto extends BaseDto implements Serializable {


    private static final long serialVersionUID = -8923930002957682525L;

    /**
     * hrs_register_record的id
     */
    private Long id;
    /**
     * 挂号日期
     */
    private String registerDate;

    /**
     * 一级科室ID
     */
    private Integer firstDptId;

    /**
     * 二级科室ID
     */
    private Integer secondDptId;

    /**
     * 患者ID
     */
    private Long patientUserId;

    /**
     * 患者姓名
     */
    private String patientUserName;

    /**
     * 患者电话
     */
    private String patientPhoneNum;

    /**
     * 医生ID
     */
    private Long doctorUserId;

    /**
     * 1:上午，2：下午
     */
    private Integer timeFlag;

    /**
     * 1:正常挂号,2:追加挂号
     */
    private Integer sourceType;

    /**
     * 挂号费用
     */
    private BigDecimal registerCharges;

    /**
     * 挂号状态
     */
    private Integer status;

    /**
     * 医生级别（1：普通，2：专家）
     */
    private Integer doctorLevel;

}
