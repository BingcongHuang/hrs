package com.hrs.cloud.controller;

import com.hrs.cloud.base.BaseController;
import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.base.ResultCode;
import com.hrs.cloud.entity.vo.RoleSelectVO;
import com.hrs.cloud.entity.vo.UserVO;
import com.hrs.cloud.entity.vo.operatorUser.OperatorUser;
import com.hrs.cloud.entity.vo.operatorUser.OperatorUserListVO;
import com.hrs.cloud.service.DepartmentService;
import com.hrs.cloud.service.OperatorService;
import com.hrs.cloud.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台设置 （权限 用户 角色）
 */
@Controller
@RequestMapping("/setting")
public class SettingsController extends BaseController {

    @Resource
    private RoleService roleService;
    @Resource
    private OperatorService operatorService;
    @Resource
    private DepartmentService departmentService;

    /**
     * 后台用户列表
     */
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public String getUserList(Model model) {
        List<RoleSelectVO> roleSelectVOS = roleService.getRoleSelect();
        model.addAttribute("roleSelect",roleSelectVOS);
        model.addAttribute("dptInfos",departmentService.findAllFirstDpt());
        return "system/user";
    }

    /**
     * 后台用户列表
     */
    @RequestMapping(value = "/user/list", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    PageVO<OperatorUserListVO> getUserListPage(@RequestParam(value = "page") Integer page, @RequestParam(value = "limit") Integer limit) {
        PageVO<OperatorUserListVO> pageVO = operatorService.getOperatorUserListVO(page, limit);
        return pageVO;
    }

    /**
     * 后台用户编辑
     */
    @RequestMapping(value = "/user/edit", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Result editUser(@RequestBody OperatorUser operatorUser) {
        Result result = new Result();
        UserVO userVO = getCurrentUserVO();
        if (userVO != null) {
            if(operatorUser.getUserId() != null) {
                result = operatorService.modifyOperatorUser(operatorUser, userVO.getUserId());
            } else {
                result = operatorService.addUser(operatorUser, userVO.getUserId());
            }
            return result;
        } else {
            result.setCode(ResultCode.NO_LOGIN);
            result.setMessage("not login");
            return result;
        }
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.GET)
    public String addEditPage(@RequestParam(value = "userId") Long userId,Model model) {

        Result result = operatorService.userDetail(userId);
        OperatorUser operatorUser = (OperatorUser)result.getData();
        model.addAttribute("operatorUser",operatorUser);
//        List<RoleSelectVO> roleSelectVOS = roleService.getRoleSelect();
//        model.addAttribute("roleSelect",roleSelectVOS);
        return "setting/userEdit";
    }

    /**
     * 后台用户删除
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/user/del", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Result delUser(@RequestParam(value = "userId") Long userId) {
        UserVO userVO = getCurrentUserVO();
        if (userVO != null) {
            Result result = operatorService.delUser(userId, userVO.getUserId());
            return result;
        } else {
            Result result = new Result();
            result.setCode(ResultCode.NO_LOGIN);
            result.setMessage("not login");
            return result;
        }
    }

    /**
     * 后台用户禁用
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/user/disable", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Result disbaleUser(@RequestParam(value = "userId") Long userId) {
        UserVO userVO = getCurrentUserVO();
        if (userVO != null) {
            Result result = operatorService.modifyUserLock(userId, true, userVO.getUserId());
            return result;
        } else {
            Result result = new Result();
            result.setCode(ResultCode.NO_LOGIN);
            result.setMessage("not login");
            return result;
        }
    }

    /**
     * 后台用户启用
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/user/enable", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Result enableUser(@RequestParam(value = "userId") Long userId) {
        UserVO userVO = getCurrentUserVO();
        if (userVO != null) {
            Result result = operatorService.modifyUserLock(userId, false, userVO.getUserId());
            return result;
        } else {
            Result result = new Result();
            result.setCode(ResultCode.NO_LOGIN);
            result.setMessage("not login");
            return result;
        }
    }


    /**
     * 后台用户添加
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Result addUser(@RequestBody OperatorUser operatorUser) {
        UserVO userVO = getCurrentUserVO();
        if (userVO != null) {
            Result result = operatorService.addUser(operatorUser, userVO.getUserId());
            return result;
        } else {
            Result result = new Result();
            result.setCode(ResultCode.NO_LOGIN);
            result.setMessage("not login");
            return result;
        }

    }

    @GetMapping(value = "/user/add")
    public String addUserPage(Model model) {
        List<RoleSelectVO> roleSelectVOS = roleService.getRoleSelect();
        model.addAttribute("roleSelect",roleSelectVOS);
        return "/system/userAdd";
    }

}
