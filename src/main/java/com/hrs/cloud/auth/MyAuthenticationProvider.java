
package com.hrs.cloud.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * description:登录验证
 *
 * @author bingcong huang
 */
@Component
public class MyAuthenticationProvider extends DaoAuthenticationProvider {
    @Autowired
    @Qualifier("MyuserDetailsService")
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            //调用上层验证逻辑
            Authentication auth = super.authenticate(authentication);
            return auth;
        } catch (Exception e) {
            throw e;
        }
    }
}
