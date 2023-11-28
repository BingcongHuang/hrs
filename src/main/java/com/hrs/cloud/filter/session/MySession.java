package com.hrs.cloud.filter.session;


import com.hrs.cloud.helper.RedisHelper;
import com.hrs.cloud.helper.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.util.Enumeration;
import java.util.UUID;

/**
 * 自定义session
 */
@Slf4j
@DependsOn("applicationContextUtil")
@SpringBootConfiguration
public class MySession  implements HttpSession {

    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    /**
     * session的过期时间,延长到3小时
     */
    private final int sessionTimeout=60*60*3;
    private Cookie[] cookies;

    private String sessionId;
    public MySession(){}

    public MySession(HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse, Cookie[] cookies){

        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.cookies = cookies;
        this.sessionId = getCookieSessionId(cookies);
    }


    /**
     * 获取指定属性值
     * @param key 属性key
     * @return  key对应的value
     */
    @Override
    public String getAttribute(String key) {
        if(sessionId != null){
            return redisHelper.get(sessionId+SESSION_SPILT+key);
        }else{
            sessionId = getCookieSessionId(cookies);
            if(sessionId !=null){
                return redisHelper.get(sessionId+SESSION_SPILT+key);
            }
        }
        return null;
    }




    /**
     * 之前说过了，此类属性不能注入,只能通过手动获取
     */

    private final RedisHelper redisHelper = ApplicationContextUtil.getBean("redisHelper",RedisHelper.class);

    //sessionId 的前缀
    private static final String SESSIONID_PRIFIX="xhlm";
    private static final String SESSION_SPILT=":";

    /**
     * 设置属性值
     * @param key           key
     * @param value         value
     */
    @Override
    public void setAttribute(String key, Object value) {
        if (sessionId != null) {
            if (value instanceof String){
                redisHelper.setex(sessionId+SESSION_SPILT+key,sessionTimeout,value.toString());
            }else{
                redisHelper.setex(sessionId+SESSION_SPILT+key,sessionTimeout, JsonHelper.toJson(value));
            }
        }else{
            //如果是第一次登录,那么生成 sessionId,将属性值存入redis,设置过期时间,并设置浏览器cookie
            this.sessionId = SESSIONID_PRIFIX + UUID.randomUUID();
            setCookieSessionId(sessionId);
            if (value instanceof String){
                redisHelper.setex(sessionId+SESSION_SPILT+key,sessionTimeout,value.toString());
            }else{
                redisHelper.setex(sessionId+SESSION_SPILT+key,sessionTimeout,JsonHelper.toJson(value));
            }
        }
    }





    //将sessionId存入浏览器
    private void setCookieSessionId(String sessionId){
        Cookie cookie = new Cookie(SESSIONID,sessionId);
        cookie.setPath("/");
        cookie.setMaxAge(sessionTimeout);
        cookie.setHttpOnly(true);
        this.httpServletResponse.addCookie(cookie);
    }

    /**
     * 移除指定的属性
     * @param key  属性 key
     */
    @Override
    public void removeAttribute(String key) {
        if(sessionId != null){
            redisHelper.del(sessionId+SESSION_SPILT+key);
        }
    }



    /**
     * session失效,删除当前用户的所有信息
     */
    @Override
    public void invalidate() {
        //删除在 redis 中的信息
//        if(sessionId != null){
//            redisHelper.delKeys(sessionId+SESSION_SPILT);
//            sessionId = null;
//            if(cookies != null){
//                for(Cookie cookie : cookies){
//                    if(SESSIONID.equals(cookie.getName())){
//                        //使cookie无效
//                        cookie.setMaxAge(0);
//                        cookie.setPath("/");
//                        httpServletResponse.addCookie(cookie);
//                        cookies = null;
//                        break;
//                    }
//                }
//            }
//        }
    }

    @Override
    public boolean isNew() {
        return false;
    }

    //浏览器的cookie key
    private static final String SESSIONID="xhlmmanager";

    //从浏览器获取SessionId
    private String getCookieSessionId(Cookie[] cookies){
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(SESSIONID.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }



    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public void putValue(String name, Object value) {

    }

    @Override
    public void removeValue(String name) {

    }

    @Override
    public Object getValue(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

}
