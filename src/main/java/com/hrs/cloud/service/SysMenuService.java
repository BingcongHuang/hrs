package com.hrs.cloud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hrs.cloud.constants.RedisRoleCache;
import com.hrs.cloud.dao.SysPermissionMapper;
import com.hrs.cloud.dao.SysRolePermissionRelationMapper;
import com.hrs.cloud.entity.SysPermission;
import com.hrs.cloud.entity.SysRolePermissionRelation;
import com.hrs.cloud.entity.vo.SysPermissionVO;
import com.hrs.cloud.helper.RedisHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (SysMenu)表服务实现类
 *
 * @author bingcong huang
 */
@Service("sysMenuService")
public class SysMenuService {
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysRolePermissionRelationMapper sysRolePermissionRelationMapper;
    @Resource
    private RedisHelper redisUtils;

    /**
     *  二级目录 如果需要三级目录再进行改造
     */

    private List<SysPermissionVO> getAllMenu(Integer category){

        //查询类型为目录和菜单的menu
        QueryWrapper<SysPermission> sysMenuQueryWrapper=new QueryWrapper<>();
        sysMenuQueryWrapper.in("RES_TYPE", 1,3).eq("DELETED", 0).orderByAsc("PARENT_ID").orderByAsc("MENU_ORDER");
        if(category!=null){
            sysMenuQueryWrapper.eq("category",category);
        }
        List<SysPermission> sysMenus =sysPermissionMapper.selectList(sysMenuQueryWrapper);
        List<SysPermissionVO> list = new ArrayList<>();
        Map<Long,SysPermissionVO> map = new HashMap<>();
        for(SysPermission sysMenu : sysMenus){
            SysPermissionVO sysMenuVO=new SysPermissionVO();
            if(sysMenu.getParentId().equals(0L)){
                //父节点为0的是根节点
                BeanUtils.copyProperties(sysMenu,sysMenuVO);
                map.put(sysMenu.getId(),sysMenuVO);
                list.add(sysMenuVO);
            }else{
                BeanUtils.copyProperties(sysMenu,sysMenuVO);
                SysPermissionVO parentVO = map.get(sysMenu.getParentId());
                if (parentVO !=null){
                    List<SysPermissionVO> submenu = parentVO.getSubMenu();
                    submenu.add(sysMenuVO);
                }
            }
        }
        return list;
    }

    public List<SysPermissionVO> getPCMenu() {
        return getAllMenu(0);
    }

    public List<SysPermissionVO> getClientMenu() {
        return getAllMenu(1);
    }

    public List<SysPermissionVO> getAllMenu() {
        return getAllMenu(null);
    }

    /**
     * 根据id
     * @param id
     * @return
     */
    public SysPermission getById(Long id) {
        return sysPermissionMapper.selectById(id);
    }


    /**
     * 根据id更新数据
     * @return
     */
    public int updateById(SysPermission record) {
        record.setUpdateTime(LocalDateTime.now());
        int count = sysPermissionMapper.updateById(record);
        if (count > 0) {
            redisUtils.del(RedisRoleCache.ROLE_PERMISSION_RELATION.getKey());
        }
        return count;
    }

    /**
     * 插入新数据
     * @return
     */
    public int save(SysPermission record) {
        record.setCreateTime(LocalDateTime.now());
        record.setCategory(1);
        record.setDeleted(0);
        int count = sysPermissionMapper.insert(record);
        if (count > 0) {
            redisUtils.del(RedisRoleCache.ROLE_PERMISSION_RELATION.getKey());
        }
        return count;
    }

    /**
     * 根据id更新数据
     * @return
     */
    public int removeByIds(List<Long> idList) {
        sysPermissionMapper.deleteBatchIds(idList);
        sysRolePermissionRelationMapper.delete(new QueryWrapper<SysRolePermissionRelation>().in("PERMISSIONID", idList));
        int count = sysPermissionMapper.deleteBatchIds(idList);
        if (count > 0) {
            redisUtils.del(RedisRoleCache.ROLE_PERMISSION_RELATION.getKey());
        }
        return count;
    }


    public List<SysRolePermissionRelation> findUserRolesByRoleId(Long roleId) {
        QueryWrapper<SysRolePermissionRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ROLEID",roleId);
        return sysRolePermissionRelationMapper.selectList(queryWrapper);
    }

//    @ApiIgnore
//    @GetMapping("/role")
//    public String toRole(Model model, String menuId) {
//        model.addAttribute("menuId", menuId);
//        return "system/role";
//    }



}