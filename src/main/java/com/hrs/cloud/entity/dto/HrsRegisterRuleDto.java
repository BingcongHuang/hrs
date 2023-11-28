package com.hrs.cloud.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author bingcong huang
 * @since 2023-02-18
 */
@Data
public class HrsRegisterRuleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 开始日期
     */
    private String dateStart;

    /**
     * 结束日期
     */
    private String dateEnd;

    /**
     * 未使用，在线挂号限制数
     */
    private Integer unusedLimit;

    /**
     * 追加挂号限制数
     */
    private Integer addtionLimit;

    /**
     * 提前挂号允许的天数
     */
    private Integer earlyDaysLimit;


}
