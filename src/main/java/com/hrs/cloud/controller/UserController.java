package com.hrs.cloud.controller;


import com.hrs.cloud.base.PageVO;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.base.ResultCode;
import com.hrs.cloud.entity.dto.CustomerDto;
import com.hrs.cloud.entity.dto.SysUserDto;
import com.hrs.cloud.entity.vo.operatorUser.OperatorUser;
import com.hrs.cloud.entity.vo.operatorUser.OperatorUserListVO;
import com.hrs.cloud.service.DepartmentService;
import com.hrs.cloud.service.OperatorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * 运营后台用户操作
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private OperatorService operatorService;

    @Resource
    private DepartmentService departmentService;
    /**
     * 用户登录
     * @return
     */
    @RequestMapping(value="/login",method = RequestMethod.GET)
    public String login(){

        return "login";
    }

    /**
     * 用户注册
     * @return
     */
    @RequestMapping(value="/toRegister",method = RequestMethod.GET)
    public String toRegister(){
        return "hrs/register";
    }

    /**
     * 前端客户注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Result register(CustomerDto dto, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        Result result = new Result();
        request.setCharacterEncoding("utf-8");
        String session_vcode=(String) request.getSession().getAttribute("text");
        if(StringUtils.isEmpty(dto.getVcode())
        || !dto.getVcode().equalsIgnoreCase(session_vcode)) {
            result.setCode(ResultCode.FAIL_MESSAGE);
            result.setMessage("验证码不正确");
            return result;
        }

        OperatorUser operatorUser = new OperatorUser();
        operatorUser.setRoleId(3L);// 默认角色为顾客
        operatorUser.setUserName(dto.getUserTelphone());
        operatorUser.setUserPhone(dto.getUserTelphone());
        operatorUser.setUserPass(dto.getUserPassword());
        operatorUser.setIdType(dto.getIdType());
        operatorUser.setIdNum(dto.getIdNum());
        operatorUser.setBirthday(dto.getBirthday());
        operatorUser.setUserRealName(dto.getUserRealname());
        result = operatorService.addUser(operatorUser,-9527L);
        return result;
    }

    /**
     * 用户注册
     * @return
     */
    @RequestMapping(value="/toDoctorPage",method = RequestMethod.GET)
    public String toDoctorPage(Model model){
        model.addAttribute("dptInfos",departmentService.findAllFirstDpt());
        return "hrs/doctors";
    }

    /**
     * 医生列表数据
     */
    @PostMapping(value = "/doctor/list/find")
    @ResponseBody
    public PageVO<OperatorUserListVO> findDoctorsByDto(@RequestBody SysUserDto dto){
        return operatorService.findDoctorsByDto(dto);
    }

    /**
     * 医生列表数据
     */
    @PostMapping(value = "/doctor/list/findAll")
    @ResponseBody
    public Result findAllDoctorsByDto(SysUserDto dto){
        Result result = new Result();
        dto.setLimit(-1);
        result.setData(operatorService.findDoctorsByDto(dto).getData());
        result.setCode(ResultCode.SUCCESS);
        return result;
    }

}
