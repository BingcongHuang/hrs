package com.hrs.cloud.entity.vo.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 */
public class MenuVO implements Comparable<MenuVO>{
    /**
     * url
     */
    private String url;

    /**
     * 菜单名
     */
    private String menuName;

    private Integer menuOrder;

    private String menuIcon;


    /**
     * 子菜单
     */
    private List<MenuVO> subMenu=new ArrayList<>();
    /**
     * ID
     */
    private Long ID;

    private Long parentId;

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public List<MenuVO> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(List<MenuVO> subMenu) {
        this.subMenu = subMenu;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public int compareTo(MenuVO o) {

        if(this.getMenuOrder()!=null&&o.getMenuOrder()!=null){
            return this.getMenuOrder() - o.getMenuOrder();
        }else{
            return 0;
        }
    }
}
