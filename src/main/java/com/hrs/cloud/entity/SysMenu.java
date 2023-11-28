package com.hrs.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * (SysMenu)表实体类
 *
 * @author bingcong huang
 * @since 2022-09-10 23:35:45
 */
@SuppressWarnings("serial")
@TableName("sys_menu")
public class SysMenu extends Model<SysMenu> {

    @TableId(value = "menu_id",type = IdType.AUTO)
    private Long menuId;
    //父菜单ID，一级菜单为0
    private Long parentId;
    //菜单名称
    private String name;
    //菜单URL
    private String url;
    //授权(多个用逗号分隔，如：user:list,user:create)
    private String perms;
    //类型：0：目录、1：菜单、2：按钮
    private Integer type;
    //菜单图标
    private String icon;
    //排序
    private Integer orderNum;
    //0： pc菜单 1 客户端菜单
    private Integer category;


    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.menuId;
    }
}