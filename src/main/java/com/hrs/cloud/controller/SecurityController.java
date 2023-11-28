package com.hrs.cloud.controller;


import com.hrs.cloud.base.BaseController;
import com.hrs.cloud.entity.vo.UserVO;
import com.baomidou.mybatisplus.extension.api.R;
import com.hrs.cloud.base.Result;
import com.hrs.cloud.base.ResultCode;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/security")
public class SecurityController extends BaseController {
    private static String CUSTOMER = "CUSTOMER";

    /**
     * 登陆成功
     * @return
     */
    @RequestMapping(value="/loginsuccess",produces = "application/json;charset=utf-8")
    public @ResponseBody
    R loginsuccess(){
//        Result result = new Result();
//        result.setCode(ResultCode.SUCCESS);
//        result.setMessage("login success");
//        return result;
        // 如果用户为客户，则跳转H5查询页面。
        UserVO userVO = getCurrentUserVO();
        return R.ok(userVO.getCustomerFlag());
    }

    /**
     * 登录失败
     * @param request
     * @return
     */
    @RequestMapping(value="/loginfail",produces = "application/json;charset=utf-8")
    public @ResponseBody
    R loginfail(HttpServletRequest request){
        Exception exception = (Exception)request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        Result result = new Result();
        result.setCode(ResultCode.FAIL);
        String msg = "";
        if(exception!=null){
            if(exception instanceof AccountExpiredException){
                msg = "用户已过期";
//                result.setMessage("用户已过期");
            }else if(exception instanceof DisabledException){
                msg = "用户被删除";
//                result.setMessage("用户被删除");
            }else if(exception instanceof LockedException){
//                result.setMessage("用户被锁定");
                msg = "用户被锁定";
            }else  if(exception instanceof BadCredentialsException){
                msg = "用户名或者密码错误";
//                result.setMessage("用户名或者密码错误");
            } else {
                if(exception.getMessage() != null && exception.getMessage().contains("激活")){
                    msg = "exception.getMessage()";
//                    result.setMessage(exception.getMessage());
                }else {
                    msg = "未知异常";
//                    result.setMessage("fail");
                }
            }
        }else{
            msg = "未知异常";
        }

        return R.failed(msg);
    }

    /**
     * 退出成功
     * @return
     */
    @RequestMapping(value="/logoutsuccess",produces = "application/json;charset=utf-8")
    public @ResponseBody R logoutsuccess(){
//        Result result = new Result();
//        result.setCode(ResultCode.SUCCESS);
//        result.setMessage("logout success");
        return R.ok("logout success");
    }

    /**
     * 访问拒绝
     * @return
     */
    @RequestMapping(value="/denied",produces = "application/json;charset=utf-8")
    public @ResponseBody Result denied(){
        Result result = new Result();
        result.setCode(ResultCode.ACCESS_DENIED);
        result.setMessage("denied");
        return result;
    }

    /**
     * 未登录
     * @return
     */
    @RequestMapping(value="/nologin",produces = "application/json;charset=utf-8")
    public String nologin(){
//        Result result = new Result();
//        result.setCode(ResultCode.NO_LOGIN);
//        result.setMessage("nologin");
//        return result;
        return "login";
    }
}
