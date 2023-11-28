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
 * @since 2021-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ToolSearchDto extends BaseDto implements Serializable {

    /**
     * 件号
     */
    private String str;

    /**
     * 是否追击查询,0，否，1，是
     */
    private String isAppend;

    /**
     * 用户id
     */
    private String userId;
}
