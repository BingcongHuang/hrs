package com.hrs.cloud.entity.vo.menu;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SysMenuVO
 * @Description: TODO
 * @author bingcong huang
 * @Date 2022-08-26
 * @Version V1.0
 **/
@Data
public class SysMenuVO {

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

    private List<SysMenuVO> SubMenus=new ArrayList<>();


}
