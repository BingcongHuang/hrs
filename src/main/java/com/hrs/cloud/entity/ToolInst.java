package com.hrs.cloud.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
public class ToolInst implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

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
     * 设备照片编号
     */
    @TableField("PIC")
    private String pic;

    /**
     * 设备说明书编号
     */
    @TableField("GUIDE_NUM")
    private String guideNum;

    /**
     * 适用机型
     */
    @TableField("AIRCRAFT_TYPE")
    private String aircraftType;

    /**
     * 主要技术参数
     */
    @TableField("TECHNICAL_PARAM")
    private String technicalParam;

    /**
     * 手册章节
     */
    @TableField("MANUAL_CHAPTERS")
    private String manualChapters;

    /**
     * 工具设备状态
     */
    @TableField("TOOL_STATUS")
    private String toolStatus;

    /**
     * 出厂日期
     */
    @TableField("PRODUCTION_DATE")
    private String productionDate;

    /**
     * 厂家
     */
    @TableField("MERCHANT")
    private String merchant;

    /**
     * 数量 
     */
    @TableField("STOCK")
    private Integer stock;

    /**
     * 原价
     */
    @TableField("ORAGINAL_PRICE")
    private BigDecimal oraginalPrice;

    /**
     * 日租金
     */
    @TableField("DAILY_RENT")
    private BigDecimal dailyRent;

    /**
     * 所属公司
     */
    @TableField("COMPANY")
    private String company;

    /**
     * 联系人
     */
    @TableField("LINKMAN")
    private String linkman;

    /**
     * 联系电话
     */
    @TableField("PHONE_NUM")
    private String phoneNum;

    /**
     * 特别说明
     */
    @TableField("SPECIAL_DESCRIPTION")
    private String specialDescription;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

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
