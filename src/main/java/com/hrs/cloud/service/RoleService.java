package com.hrs.cloud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.base.ResultCode;
import com.hrs.cloud.constants.RedisRoleCache;
import com.hrs.cloud.constants.ResourceType;
import com.hrs.cloud.dao.SysPermissionMapper;
import com.hrs.cloud.dao.SysRoleMapper;
import com.hrs.cloud.dao.SysRolePermissionRelationMapper;
import com.hrs.cloud.dao.SysUserRoleRelationMapper;
import com.hrs.cloud.entity.SysPermission;
import com.hrs.cloud.entity.SysRole;
import com.hrs.cloud.entity.SysRolePermissionRelation;
import com.hrs.cloud.entity.SysUserRoleRelation;
import com.hrs.cloud.entity.vo.RolePermissionVO;
import com.hrs.cloud.entity.vo.RoleSelectVO;
import com.hrs.cloud.entity.vo.SysPermissionVO;
import com.hrs.cloud.entity.vo.menu.MenuVO;
import com.hrs.cloud.helper.RedisHelper;
import com.hrs.cloud.helper.json.JsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserRoleRelationMapper sysUserRoleRelationMapper;
    @Resource
    private SysRolePermissionRelationMapper sysRolePermissionRelationMapper;

    @Resource
    private RedisHelper redisUtils;

    /**
     * 角色列表不分页
     */
    public List<RoleSelectVO> getRoleSelect(){
        List<RoleSelectVO> roleSelectVOS = new ArrayList<>();
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("STATUS", 0);
        List<SysRole> sysRoles = sysRoleMapper.selectList(queryWrapper);
        if(!CollectionUtils.isEmpty(sysRoles)){
            sysRoles.forEach(x->{
                RoleSelectVO roleSelectVO = new RoleSelectVO();
                roleSelectVO.setRoleId(x.getId());
                roleSelectVO.setRole(x.getName());
                roleSelectVOS.add(roleSelectVO);
            });
        }
        return roleSelectVOS;
    }

    /**
     * 获取角色可以查看的菜单 (单角色)
     */
    public List<MenuVO> getMenuVO(String roleName) {
        List<MenuVO> menuVOS = new ArrayList<>();

        String rolePermissionRel = redisUtils.get(RedisRoleCache.ROLE_PERMISSION_RELATION.getKey());
        //缓存中是否存在
        if (StringUtils.isBlank(rolePermissionRel)) {
            List<RolePermissionVO> permissions = sysPermissionMapper.selectRolePermission();
            Map<String, List<RolePermissionVO>> rolePermissionMap = permissions.stream().collect(Collectors.groupingBy(x -> x.getRole()));
            rolePermissionRel = JsonHelper.toJson(rolePermissionMap);
            redisUtils.set(RedisRoleCache.ROLE_PERMISSION_RELATION.getKey(), rolePermissionRel);
        }

        Map<String, List<RolePermissionVO>> rolePermissionMap = JsonHelper.fromComplexJson(rolePermissionRel, new TypeReference<Map<String, List<RolePermissionVO>>>() {
        });

        List<RolePermissionVO> rolePermissionVOS = rolePermissionMap.get(roleName);
        if (!CollectionUtils.isEmpty(rolePermissionVOS)) {

            Map<Long, MenuVO> menuMap = new HashMap<>();

            Map<Long, List<RolePermissionVO>> rMap = rolePermissionVOS.stream()
                    .filter(x -> ResourceType.MENU.equals(x.getResType()))
                    .collect(Collectors.groupingBy(x -> x.getParentId()));

            rMap.forEach((y, z) -> {
                if (y == 0) {
                    z.forEach(m -> {
                        MenuVO menuVO = new MenuVO();
                        menuVO.setID(m.getId());
                        menuVO.setMenuName(m.getDescription());
                        menuVO.setUrl(m.getPermission());
                        menuVO.setMenuOrder(m.getMenuOrder());
                        menuVO.setMenuIcon(m.getMenuIcon());
                        menuVO.setParentId(m.getParentId());
                        menuVOS.add(menuVO);
                        menuMap.put(m.getId(), menuVO);
                    });
                }
            });
            rMap.forEach((y, z) -> {
                if (y != 0) {
                    z.forEach(m -> {
                        MenuVO pmenuVO = menuMap.get(y);
                        MenuVO menuVO = new MenuVO();
                        menuVO.setID(m.getId());
                        menuVO.setMenuName(m.getDescription());
                        menuVO.setUrl(m.getPermission());
                        menuVO.setParentId(m.getParentId());
                        pmenuVO.getSubMenu().add(menuVO);
                    });
                }
            });

        }
        List<MenuVO> menuVOList = menuVOS.stream().sorted(Comparator.comparing(MenuVO::getMenuOrder)).collect(Collectors.toList());
        return menuVOList;
    }

    /**
     * 角色列表分页
     */

    public PageVO<SysRole> getRoleListVO(Integer page, Integer limit) {

        PageVO<SysRole> pageVO = new PageVO<SysRole>();

        Page<SysRole> pg = new Page<>();
        pg.setCurrent(page);
        pg.setSize(limit);
        IPage<SysRole> pageres = sysRoleMapper.selectPage(pg, new QueryWrapper<SysRole>().eq("STATUS", 0));
        pageVO.setCode(0);
        pageVO.setMsg("");
        pageVO.setData(pageres.getRecords());
        pageVO.setCount(pageres.getTotal());
        return pageVO;
    }

    /**
     * 添加角色
     */

    public Result addRole(SysRole roleAddVO) {
        Result result = new Result();
        int i = sysRoleMapper.selectCount(new QueryWrapper<SysRole>()
                .eq("ROLE", roleAddVO.getRole())
                .eq("STATUS", 0));
        if (i > 0) {
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("角色已存在");
        } else {
            SysRole sysRole = new SysRole();
            sysRole.setName(roleAddVO.getName());
            sysRole.setCreateTime(LocalDateTime.now());
            sysRole.setDescription(roleAddVO.getDescription());
            sysRole.setStatus(0);
            sysRole.setRole(roleAddVO.getRole());
            int m = sysRoleMapper.insert(sysRole);
            if (m > 0) {
                result.setCode(ResultCode.SUCCESS);
            } else {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("角色创建失败");
            }
        }
        return result;
    }

    /**
     * 删除角色
     */

    public Result delRole(Long roleId) {
        Result result = new Result();

        int ucount = sysUserRoleRelationMapper.selectCount(new QueryWrapper<SysUserRoleRelation>().eq("ROLEID", roleId));
        if (ucount > 0) {
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("该角色下有关联用户");
            return result;
        } else {
            SysRole sysRole = new SysRole();
            sysRole.setStatus(2);
            sysRole.setUpdateTime(LocalDateTime.now());
            int i = sysRoleMapper.update(sysRole, new QueryWrapper<SysRole>().eq("ID", roleId));
            if (i > 0) {
                result.setCode(ResultCode.SUCCESS);
                result.setMessage("角色删除成功");
            } else {
                result.setCode(ResultCode.FAIL_MESSAGE);
                result.setMessage("角色删除成功");
            }
            return result;
        }
    }


    /**
     * 角色详情
     */
    public Result roleDetail(Long roleId) {
        Result result = new Result();
        SysRole sysRole = sysRoleMapper.selectById(roleId);
        if (null != sysRole) {
            result.setCode(ResultCode.SUCCESS);
            result.setData(sysRole);
        } else {
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("角色不存在");
        }

        return result;
    }

    /**
     * 修改角色
     */
    public Result editRole(SysRole roleEditVO) {
        Result result = new Result();
        SysRole sysRole = new  SysRole();
        sysRole.setId(roleEditVO.getId());
        sysRole.setDescription(roleEditVO.getDescription());
        sysRole.setName(roleEditVO.getName());
        sysRole.setUpdateTime(LocalDateTime.now());
        sysRole.setRole(roleEditVO.getRole());
        int i = sysRoleMapper.updateById(sysRole);

        if(i>0){
            result.setCode(ResultCode.SUCCESS);
            result.setMessage("修改角色成功");
        }else {
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("修改角色失败");
        }

        return result;
    }


    /**
     * 获取所有权限
     */
    public Map<Long,List<SysPermissionVO>> getAllAuthorize(){

      List<SysPermission>  sysPermissions = sysPermissionMapper.selectPermissionsAll();

      Map<Long,String> maps = new HashMap<>();
        sysPermissions.forEach(x->{
            maps.put(x.getId(),x.getDescription());
        });

      Map<Long,List<SysPermissionVO>> permissionVOMap = new HashMap<>();

        sysPermissions.forEach(x->{
            SysPermissionVO pv = new SysPermissionVO();
            pv.setDescription(x.getDescription());
            pv.setId(x.getId());
            pv.setParentId(x.getParentId());
            if(x.getParentId()==0||x.getParentId()==-1){
                if(permissionVOMap.get(x.getId())==null){
                    List<SysPermissionVO> sp =new ArrayList<SysPermissionVO>();
                    sp.add(pv);
                    permissionVOMap.put(x.getId(),sp);
                }else{
                    permissionVOMap.get(x.getId()).add(pv);
                }

            }else{
                if(permissionVOMap.get(x.getParentId())==null){
                    List<SysPermissionVO> sp = new ArrayList<>();
                    sp.add(pv);
                    permissionVOMap.put(x.getParentId(),sp);
                }else{
                    permissionVOMap.get(x.getParentId()).add(pv);
                }
            }

        });


        return permissionVOMap;


    }

    /**
     * 赋权给用户
     * @return
     */
    public Result authorizeToUser(Long roleId,String permissionId){

        Result result = new Result();
        sysRolePermissionRelationMapper.delete(new QueryWrapper<SysRolePermissionRelation>().eq("ROLEID", roleId));

        String[] s = permissionId.split(",");
        for(String str:s){
            SysRolePermissionRelation sysRolePermissionRelation = new SysRolePermissionRelation();
            sysRolePermissionRelation.setPermissionid(Long.parseLong(str));
            sysRolePermissionRelation.setRoleid(roleId);
            sysRolePermissionRelationMapper.insert(sysRolePermissionRelation);
        }
        result.setCode(ResultCode.SUCCESS);
        redisUtils.del(RedisRoleCache.ROLE_PERMISSION_RELATION.getKey());
        return result;
    }
}
