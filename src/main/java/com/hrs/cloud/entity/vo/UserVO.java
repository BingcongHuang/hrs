/*
 * Project Name:ammunition-code
 * File Name:UserVO
 * Package Name:com.gaoxing.vo
 * Copyright (c) 2017,314059610@qq.com All Rights Reserved.
 */
package com.hrs.cloud.entity.vo;


import com.hrs.cloud.entity.SysRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * description:用户
 *
 * @author bingcong huang
 */
public class UserVO extends User {


    private static final long serialVersionUID = 7720303024643375850L;
    // 用户ID
    private Long  userId ;
    // 用户名
    private String userName;
    // 真名
    private String realName;
    // 手机号
    private String telPhone;
    //
    private List<SysRole> roles;

    private Integer customerFlag;

    private String birthday;
    

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	public UserVO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserVO(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public Integer getCustomerFlag() {
        return customerFlag;
    }

    public void setCustomerFlag(Integer customerFlag) {
        this.customerFlag = customerFlag;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object rhs) {  
        if (rhs instanceof UserVO) {
            return userName.equals(((UserVO) rhs).userName);  
        }  
        return false;  
    }  
      
    /** 
     * Returns the hashcode of the {@code username}. 
     */  
    @Override  
    public int hashCode() {
    	return userName.hashCode();  
    }  
    
}
