package com.hrs.cloud.filter.session;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * session拦截器
 */
@Slf4j
//@WebFilter(filterName="sessionFilter",urlPatterns="/*")
public class SessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //将请求替换成自定义的 request
        HttpServletRequest sessionHttpServletRequestWrapper =
                new SessionHttpServletRequestWrapper(
                        (HttpServletRequest)request,(HttpServletResponse)response);
        //获取请求的路径
        String sessionPath= sessionHttpServletRequestWrapper.getServletPath();
        //如果是与Session操作有关的请求,就替换成自定请求包装类,SESSION_PATH是我程序中的
        //一个常量，也就是你需要操作Session的那个RequestMapping路径
        //并继续调用doFilter执行责任链
        String[] exclusionsUrls = {".js",".gif",".jpg",".png",".css",".ico"};
        for (String str : exclusionsUrls) {
            if (sessionPath.contains(str)) {
                chain.doFilter(request,response);
                return;
            }
        }
        //如果是普通请求就放行
        chain.doFilter(sessionHttpServletRequestWrapper ,response);
    }

    @Override
    public void destroy() {

    }
}
