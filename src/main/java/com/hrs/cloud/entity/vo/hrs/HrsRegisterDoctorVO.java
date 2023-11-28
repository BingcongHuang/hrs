package com.hrs.cloud.entity.vo.hrs;

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
public class HrsRegisterDoctorVO implements Serializable {


    private static final long serialVersionUID = -8923930002957682525L;

    /**
     * 医生Id
     */
    private Long doctorId;

    /**
     * 医生姓名
     */
    private String doctorName;
    /**
     * 医生简介
     */
    private String doctorIntroduction;

    /**
     * 上午班（0：否，1：是,2:停诊）
     */
    private Integer moningFlag;

    /**
     * 下午班（0：否，1：是，2:停诊）
     */
    private Integer afternoonFlag;

    /**
     * 医生级别（1：普通，2：专家）
     */
    private Integer doctorLevel;

    /**
     * 挂号费用
     */
    private BigDecimal registerCharges;


    /**
     * 上午剩余可预订数
     */
    private Integer moningNums;

    /**
     * 下午午剩余可预订数
     */
    private Integer afternoonNums;
}
