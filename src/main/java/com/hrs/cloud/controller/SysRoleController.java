package com.hrs.cloud.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.entity.SysRole;
import com.hrs.cloud.entity.SysRolePermissionRelation;
import com.hrs.cloud.service.RoleService;
import com.hrs.cloud.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (SysRole)表控制层
 *
 * @author bingcong huang
 * @since 2022-08-20 20:37:42
 */
@ApiIgnore
@Controller
@RequestMapping("role")
public class SysRoleController extends ApiController {

    @Resource
    private RoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @ApiIgnore
    @GetMapping("")
    public String toRole(Model model, String menuId) {
        model.addAttribute("menuId", menuId);
        return "system/role";
    }

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @return 所有数据
     */
    @PostMapping("/findRolePg")
    @ResponseBody
    public PageVO<SysRole> selectAll(Integer page, Integer limit) {
        return sysRoleService.getRoleListVO(page,limit);
    }

    @ApiIgnore
    @GetMapping("/toSave")
    public String toSave(Long id, String type, Model model) {
        if (type.equals("add")) {
            model.addAttribute("id", "");
            model.addAttribute("role", new SysRole());
        } else {
            SysRole sysRole = (SysRole)sysRoleService.roleDetail(id).getData();
            model.addAttribute("id", id);
            model.addAttribute("role", sysRole);
        }
        return "system/role-save";
    }

    @PostMapping("/saveRole")
    @ResponseBody
    public Result saveRole(SysRole sysRole) {
        if (sysRole.getId() == null){
            return this.sysRoleService.addRole(sysRole);
        }else{
            sysRole.setCreateTime(LocalDateTime.now());
            return this.sysRoleService.editRole(sysRole);
        }
    }
    /**
     * 删除数据
     *
     * @param roleId 主键结合
     * @return 删除结果
     */
    @PostMapping("/removeRole")
    @ResponseBody
    public Result delete(@RequestParam("id") Long roleId) {
        return this.sysRoleService.delRole(roleId);
    }


    @ApiIgnore
    @GetMapping("/toRoleMenus")
    public String toRoleMenus(Long id, Model model) {
        List<SysRolePermissionRelation> list = sysMenuService.findUserRolesByRoleId(id);
        List<Long> ids = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)) {
            ids = list.stream().map(a -> a.getPermissionid()).collect(Collectors.toList());
        }
        model.addAttribute("roleId", id);
        model.addAttribute("ids", ids);
        model.addAttribute("pcmenus", sysMenuService.getPCMenu());
        model.addAttribute("clientmenus", sysMenuService.getClientMenu());
        return "system/role-menus";
    }

    @PostMapping("/saveRoleMenus")
    @ResponseBody
    public Result saveRoleMenus(Long roleId, String menuIds) {
        return sysRoleService.authorizeToUser(roleId, menuIds);
    }

//    /**
//     * 删除数据
//     *
//     * @param idList 主键结合
//     * @return 删除结果
//     */
//    @PostMapping("/removeRole")
//    @ResponseBody
//    public R delete(@RequestParam("idList") List<Long> idList) {
//        return success(this.sysRoleService.removeByIds(idList));
//    }
//
//    @ApiIgnore
//    @GetMapping("/toSave")
//    public String toSave(Integer id, String type, Model model) {
//        if (type.equals("add")) {
//            model.addAttribute("id", "");
//            model.addAttribute("role", SysRole.builder().build());
//        } else {
//            SysRole sysRole = sysRoleService.getById(id);
//            model.addAttribute("id", id);
//            model.addAttribute("role", sysRole);
//        }
//        return "system/role-save";
//    }
//
//
//
//    @PostMapping("/saveRole")
//    @ResponseBody
//    public R saveRole(SysRole sysRole) {
//        if (sysRole.getRoleId() !=null){
//            return success(this.sysRoleService.updateById(sysRole));
//        }else{
//            sysRole.setCreateTime(new Date());
//            return success(this.sysRoleService.save(sysRole));
//        }
//    }
//
//
//    @ApiIgnore
//    @GetMapping("/toRoleMenus")
//    public String toRoleMenus(Long id, Model model) {
//        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<SysRoleMenu>();
//        queryWrapper.in("role_id",id);
//        List<SysRoleMenu> list = sysRoleMenuService.list(queryWrapper);
//        List<Long> ids = list.stream().map(a -> a.getMenuId()).collect(Collectors.toList());
//        model.addAttribute("roleId", id);
//        model.addAttribute("ids", ids);
//        model.addAttribute("pcmenus", sysMenuService.getPCMenu());
//        model.addAttribute("clientmenus", sysMenuService.getClientMenu());
//
//        return "system/role-menus";
//    }
//
//    @PostMapping("/saveRoleMenus")
//    @ResponseBody
//    public R saveRoleMenus(Long roleId, String menuIds) {
//        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.in("role_id",roleId);
//        sysRoleMenuService.remove(queryWrapper);
//        String[] split = menuIds.split(",");
//        List<String> strings = Arrays.asList(split);
//        List<SysRoleMenu> collect = strings.stream().map(a -> {
//            return SysRoleMenu.builder()
//                    .menuId(Long.valueOf(a))
//                    .roleId(roleId).build();
//        }).collect(Collectors.toList());
//        boolean b = sysRoleMenuService.saveBatch(collect);
//        if (b){
//            return success(b);
//        }else{
//            return failed("保存权限信息失败！");
//        }
//    }
//
//    @ApiIgnore
//    @GetMapping("roleCheck")
//    public String toRoleCheck(Model model) {
//
//        QueryWrapper queryWrapper=new QueryWrapper();
//        List<SysRole> sysRoles = sysRoleService.list(queryWrapper);
//        model.addAttribute("sysRoles",sysRoles);
//        return "system/role-check";
//    }


}