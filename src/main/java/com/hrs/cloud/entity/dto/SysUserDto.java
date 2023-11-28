package com.hrs.cloud.entity.dto;

import com.hrs.cloud.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author bingcong huang
 * @since 2023-02-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    private String userTelphone;

    /**
     * 真实姓名
     */
    private String userRealname;

    /**
     * 一级科室ID(医生用)
     */
    private Integer firstDptId;

    /**
     * 二级科室ID(医生用)
     */
    private Integer secondDptId;

    /**
     * 医生级别（1：普通，2：专家）
     */
    private Integer doctorLevel;


}
