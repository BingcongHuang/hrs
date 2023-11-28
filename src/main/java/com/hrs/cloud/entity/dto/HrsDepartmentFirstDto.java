package com.hrs.cloud.entity.dto;

import com.hrs.cloud.base.BaseDto;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 门诊科室（一级）
 * </p>
 *
 * @author bingcong huang
 * @since 2023-02-07
 */
@Data
public class HrsDepartmentFirstDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 3688935247566545866L;
    /**
     * 科室号
     */
    private Integer id;

    /**
     * 科室中文名称
     */
    private String dptCname;

    /**
     * 状态（0：禁用，1：启用）
     */
    private Integer status;

}
