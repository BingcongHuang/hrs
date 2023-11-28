package com.hrs.cloud.entity.dto;

import com.hrs.cloud.base.BaseDto;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

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
public class ToolInstDto extends BaseDto implements Serializable {

    /**
     * 件号
     */
    @TableField("TOOL_CODE")
    private String toolCode;

    /**
     * 序号
     */
    @TableField("SEQ_NO")
    private String seqNo;

    /**
     * 名称
     */
    @TableField("TOOL_NAME")
    private String toolName;

    /**
     * 英文名称
     */
    @TableField("TOOL_ENAME")
    private String toolEname;

    /**
     * 适用机型
     */
    @TableField("AIRCRAFT_TYPE")
    private String aircraftType;

    /**
     * 工具设备状态
     */
    @TableField("TOOL_STATUS")
    private String toolStatus;

    /**
     * 出厂日期
     */
    @TableField("PRODUCTION_DATE")
    private LocalDate productionDate;

}
