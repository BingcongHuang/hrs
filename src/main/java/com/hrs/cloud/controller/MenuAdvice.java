package com.hrs.cloud.controller;

import com.hrs.cloud.base.BaseController;
import com.hrs.cloud.entity.vo.UserVO;
import com.hrs.cloud.entity.vo.menu.MenuVO;
import com.hrs.cloud.service.RoleService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import java.util.List;


@ControllerAdvice
public class MenuAdvice extends BaseController {


    @Resource
    private RoleService roleService;


    @ModelAttribute
    public Model getUserMenu(Model model) {

        UserVO userVO = this.getCurrentUserVO();
        if(userVO!=null){

            System.out.println(userVO.getUserName());
            model.addAttribute("userName",userVO.getUserName());
            if(userVO.getAuthorities().stream().findFirst()!=null){
                String roleName =userVO.getAuthorities().stream().findFirst().get().getAuthority();
                //当前角色菜单
                List<MenuVO> menuVOList = roleService.getMenuVO(roleName);
                model.addAttribute("menuVOList",menuVOList);
            }

        }
        return model;
    }


}
