package com.hrs.cloud.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomerDto implements Serializable {

    /**
     * 登录名(暂时同手机号)
     */
    private String userName;

    /**
     * 手机号
     */
    private String userTelphone;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 真实姓名
     */
    private String userRealname;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 验证码
     */
    private String vcode;

    /**
     * 证件类型（1：身份证，2：护照）
     */
    private Integer idType;

    /**
     * 证件号
     */
    private String idNum;
}
