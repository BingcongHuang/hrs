package com.hrs.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author bingcong huang
 * @since 2023-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 登录名
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 性别 0 男 1 女
     */
    @TableField("USER_SEX")
    private Integer userSex;

    /**
     * 手机号
     */
    @TableField("USER_TELPHONE")
    private String userTelphone;

    /**
     * 邮箱
     */
    @TableField("USER_EMAIL")
    private String userEmail;

    /**
     * 密码
     */
    @TableField("USER_PASSWORD")
    private String userPassword;

    /**
     * 盐
     */
    @TableField("SALT")
    private String salt;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private Long createBy;

    /**
     * 修改人
     */
    @TableField("UPDATE_BY")
    private Long updateBy;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 是否被锁定
     */
    @TableField("LOCKED")
    private Boolean locked;

    /**
     * 头像
     */
    @TableField("USER_AVATOR")
    private String userAvator;

    /**
     * 是否删除 (0 未删除 1 已删除)
     */
    @TableField("IS_DELETED")
    private Boolean isDeleted;

    /**
     * 真实姓名
     */
    @TableField("USER_REALNAME")
    private String userRealname;

    /**
     * 一级科室ID(医生用)
     */
    @TableField("FIRST_DPT_ID")
    private Integer firstDptId;

    /**
     * 二级科室ID(医生用)
     */
    @TableField("SECOND_DPT_ID")
    private Integer secondDptId;

    /**
     * 出生日期（患者用）
     */
    @TableField("BIRTHDAY")
    private LocalDate birthday;

    /**
     * 医生级别（1：普通，2：专家）
     */
    @TableField("DOCTOR_LEVEL")
    private Integer doctorLevel;

    /**
     * 证件类型（1：身份证，2：护照）
     */
    @TableField("ID_TYPE")
    private Integer idType;

    /**
     * 证件号
     */
    @TableField("ID_NUM")
    private String idNum;

    /**
     * 医生简介
     */
    @TableField("DOCTOR_INTRODUCTION")
    private String doctorIntroduction;


}
