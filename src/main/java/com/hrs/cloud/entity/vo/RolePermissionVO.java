package com.hrs.cloud.entity.vo;

public class RolePermissionVO implements Comparable<RolePermissionVO>{


    private String role;
    private String permission;
    private Byte resType;
    private String description;
    private Long parentId;
    private Long id;
    private Integer menuOrder;
    private String menuIcon;

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Byte getResType() {
        return resType;
    }

    public void setResType(Byte resType) {
        this.resType = resType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    @Override
    public int compareTo(RolePermissionVO o) {
        if(this.getMenuOrder()!=null&&o.getMenuOrder()!=null){
            return this.getMenuOrder() - o.getMenuOrder();
        }else{
            return 0;
        }

    }
}
