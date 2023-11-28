package com.hrs.cloud.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author bingcong huang
 * @since 2022-10-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysPermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 权限
     */
    @TableField("PERMISSION")
    private String permission;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

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
     * 资源类型 1: url 2:  按钮 3:菜单
     */
    @TableField("RES_TYPE")
    private Integer resType;

    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 菜单ICON
     */
    @TableField("MENU_ICON")
    private String menuIcon;

    /**
     * 排序
     */
    @TableField("MENU_ORDER")
    private Integer menuOrder;

    /**
     * 删除标识（0：未删除，1，已删除）
     */
    @TableField("DELETED")
    private Integer deleted;

    /**
     * 二级菜单
     */
    private List<SysPermissionVO> subMenu = new ArrayList<>();


}
