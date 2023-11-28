package com.hrs.cloud.controller;

import com.hrs.cloud.entity.vo.UserVO;
import com.baomidou.mybatisplus.extension.api.R;
import com.hrs.cloud.base.BaseController;
import com.hrs.cloud.entity.SysMenu;
import com.hrs.cloud.entity.SysPermission;
import com.hrs.cloud.entity.vo.SysPermissionVO;
import com.hrs.cloud.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @ClassName SystemController
 * @Author bingcong huang
 * @Date 2022-09-20
 * @Version V1.0
 **/

@Controller
@RequestMapping("/")
public class SystemController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;


    @ApiIgnore
    @GetMapping
    public String home(Model model){
//        List<SysMenuVO> sysMenuList = sysMenuService.getPCMenu();
//        model.addAttribute("menus",sysMenuList);
        //model.addAttribute("rolename","超级管理员");
//        UserVO user = getCurrentUserVO();
//        if(user != null) {
//            model.addAttribute("username", user.getRealName());
//        } else {
//            model.addAttribute("username", "匿名");
//        }
        UserVO userVO = getCurrentUserVO();
        if(null == userVO) {
            return "redirect:/user/login";
        } else {
            return "system/home";
        }
    }

    @ApiIgnore
    @GetMapping("/index")
    public String index(){
        return "system/index";
    }

    @ApiIgnore
    @GetMapping("/toMenu")
    public String toMenu(Model model){
        List<SysPermissionVO> pcMenus = sysMenuService.getAllMenu();
        model.addAttribute("pcmenus",pcMenus);
        return "system/menu";
    }

    @ApiIgnore
    @GetMapping("/menu/toSave")
    public String toSave(Long id, String type, String levels, Model model) {
        if (type.equals("add")) {
            SysMenu sysMenu = new SysMenu();
            model.addAttribute("id", "");
            model.addAttribute("parentId", id);
            model.addAttribute("menu", sysMenu);
            model.addAttribute("levels", levels);
        } else {
            SysPermission menuById = sysMenuService.getById(id);
            model.addAttribute("id", id);
            model.addAttribute("parentId", menuById.getParentId());
            model.addAttribute("menu", menuById);
            model.addAttribute("levels", levels);
        }
        return "system/menu-save";
    }

    /**
     * 修改数据
     *
     * @param sysMenu 实体对象
     * @return 修改结果
     */
    @ApiIgnore
    @PostMapping("/menu/saveMenu")
    @ResponseBody
    public R saveMenu(SysPermission sysMenu) {
        if (sysMenu.getId() !=null){
            return R.ok(this.sysMenuService.updateById(sysMenu));
        }else{
            return R.ok(this.sysMenuService.save(sysMenu));
        }
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @ApiIgnore
    @PostMapping("/menu/delete")
    @ResponseBody
    public R delete(@RequestParam("idList") List<Long> idList) {
        return  R.ok(this.sysMenuService.removeByIds(idList));
    }


}
