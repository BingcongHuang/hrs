package com.hrs.cloud.base;


import com.hrs.cloud.entity.vo.UserVO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    /**
     * 获取当前登录用户
     * @return
     */
    public UserVO getCurrentUserVO(){
        UserVO userDetails = null;
        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            userDetails = (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return userDetails;
    }

}
