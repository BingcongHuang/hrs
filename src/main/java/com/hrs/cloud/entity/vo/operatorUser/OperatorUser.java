package com.hrs.cloud.entity.vo.operatorUser;

public class OperatorUser extends OperatorUserListVO {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户密码
     */
    private String userPass;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

}
