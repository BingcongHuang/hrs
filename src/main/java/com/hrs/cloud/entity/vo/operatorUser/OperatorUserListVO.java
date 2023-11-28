package com.hrs.cloud.entity.vo.operatorUser;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperatorUserListVO {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 登录名
     */
    private String userName;
    /**
     * 性别
      */
    private Integer userSex;
    /**
     * 用户手机
     */
    private String userPhone;
    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 用户真实姓名
     */
    private String userRealName;

    /**
     * 是否锁定
     */
    private Boolean locked;


    /**
     * 一级科室ID(医生用)
     */
    private Integer firstDptId;

    /**
     * 二级科室ID(医生用)
     */
    private Integer secondDptId;

    /**
     * 一级科室名称(医生用)
     */
    private String firstDptStr;

    /**
     * 二级科室名称(医生用)
     */
    private String secondDptStr;

    /**
     * 生日（患者用）
     */
    private String birthday;

    /**
     * 医生级别（1：普通，2：专家）
     */
    private Integer doctorLevel;

    /**
     * 显示用角色id
     */
    private Long role;

    /**
     * 显示用角色名称
     */
    private String roleName;

    /**
     * 证件类型（1：身份证，2：护照）
     */
    private Integer idType;

    /**
     * 证件号
     */
    private String idNum;

    /**
     * 医生简介
     */
    private String doctorIntroduction;
}
