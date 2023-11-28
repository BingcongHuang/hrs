package com.hrs.cloud.interceptor;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UrlInterceptor implements HandlerInterceptor {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(UrlInterceptor.class);


    @Value("${mainDomain}")
    private String mainDomain;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {

        String XRequested =httpServletRequest.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(XRequested)){

            return true;
        }else{
            if(httpServletRequest.getRequestURI().indexOf("security/nologin")>-1){
                httpServletResponse.sendRedirect(mainDomain+"/cloud/user/login");
               return false;
            }else{
                return true;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
