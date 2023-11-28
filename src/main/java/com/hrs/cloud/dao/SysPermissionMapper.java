package com.hrs.cloud.dao;

import com.hrs.cloud.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrs.cloud.entity.vo.RolePermissionVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bingcong huang
 * @since 2022-10-01
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    List<RolePermissionVO> selectRolePermission();

    List<SysPermission> selectPermissionsAll();

}
