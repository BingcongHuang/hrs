package com.hrs.cloud.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version V1.0
 * @project：
 * @title：CookieUtils.java
 * @description：
 * @package com.hrs.cloud.helper
 * @copyright: 医院挂号云平台  All rights reserved.
 */
public class CookieUtils {

    // 默认缓存时间,单位/秒, 2H
    public static final int COOKIE_DEFAULT_AGE = 60 * 60 * 2;

    // 保存路径,根路径
    private static final String COOKIE_PATH = "/";

    /**
     * @title: set
     * @description: 保存cookie, 默认2H
     * @param: response
     * @param: key
     * @param: value
     * @return: void
     */
    public static void set(HttpServletResponse response, String key, String value) {
        set(response, key, value, null, COOKIE_PATH, COOKIE_DEFAULT_AGE, true);
    }

    /**
     * @title: set
     * @description: 保存cookie
     * @param: response
     * @param: key
     * @param: value
     * @Param maxAge:过期时间
     * @return: void
     */
    public static void set(HttpServletResponse response, String key, String value, int maxAge) {
        set(response, key, value, null, COOKIE_PATH, maxAge, true);
    }

    /**
     * @title: set
     * @description: 保存cookie
     * @param: response
     * @param: key
     * @param: value
     * @param: domain
     * @param: path
     * @param: maxAge
     * @param: isHttpOnly
     * @return: void
     */
    private static void set(HttpServletResponse response, String key, String value, String domain, String path, int maxAge, boolean isHttpOnly) {
        Cookie cookie = new Cookie(key, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }

    /**
     * @title: getValue
     * @description: 查询cookie
     * @param: request
     * @param: key
     * @return: java.lang.String
     */
    public static String getValue(HttpServletRequest request, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * @title: get
     * @description: 查询cookie
     * @param: request
     * @param: key
     * @return: javax.servlet.http.Cookie
     */
    private static Cookie get(HttpServletRequest request, String key) {
        Cookie[] arr_cookie = request.getCookies();
        if (arr_cookie != null && arr_cookie.length > 0) {
            for (Cookie cookie : arr_cookie) {
                if (cookie.getName().equals(key)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * @title: remove
     * @description: 删除cookie
     * @param: request
     * @param: response
     * @param: key
     * @return: void
     */
    public static void remove(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            set(response, key, "", null, COOKIE_PATH, 0, true);
        }
    }
}
