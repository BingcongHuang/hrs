
package com.hrs.cloud.auth;


import com.hrs.cloud.helper.RedisHelper;
import com.hrs.cloud.dao.SysPermissionMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;


/**
 * description:security config
 * EnableGlobalMethodSecurity 添加注解支持
 * 1. securedEnabled   类似@security
 * 2. jsr250Enabled
 * 3. prePostEnabled   类似@PreAuthorize("isAnonymous()") 表达式支持
 *
 * @author bingcong huang
 */
@Configuration
//@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LogManager.getLogger();

    @Resource
    private MyUserDetailService userDetailsService;
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private MyAuthenticationProvider myAuthenticationProvider;
    @Resource
    private RedisHelper redisUtils;

    @Resource
    public void configureGlobal(
            AuthenticationManagerBuilder auth) throws Exception {
        // 配置密码加密方式为MD5加密
        myAuthenticationProvider.setPasswordEncoder(new MessageDigestPasswordEncoder("MD5"));
        auth.authenticationProvider(myAuthenticationProvider);

    }

    /**
     * 未登录时候的accessDenied和登录之后的accessDenied是不一样的
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.addFilterAfter(MyUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加例外
        http.authorizeRequests()
                .antMatchers("/user/login")
                .anonymous();
        http.authorizeRequests()
                .antMatchers("/security/**")
                .anonymous();
        http.authorizeRequests()
                .antMatchers("/hrs/**")
                .anonymous();
        //静态资源直接放行
        http.authorizeRequests()
                .antMatchers("/statics/**")
                .anonymous();

        http.authorizeRequests().anyRequest().authenticated().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                fsi.setSecurityMetadataSource(myInvocationSecurityMetadataSource());
                fsi.setAccessDecisionManager(accessDecisionManager());
                fsi.setAuthenticationManager(authenticationManagerBean());
                return fsi;
            }
        }).and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/security/nologin"))
                .and().anonymous().disable();

        http.exceptionHandling().accessDeniedPage("/security/denied");
        //不需要csrf_token
         http.csrf().disable();
        // 登录
        http.formLogin().loginPage("/user/login")
                .successForwardUrl("/user/loginsuccess")//成功之后跳转 返回json对象 方便ajax跳转
                .failureForwardUrl("/user/loginfail")//失败之后跳转
                .permitAll();
        // 注销
        http.logout().deleteCookies("remove").invalidateHttpSession(true)
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/user/login")
                .permitAll();
        //记住密码
        http.rememberMe().key("H4DCNlePRVGfsPSyOR0ggw==")
                .tokenValiditySeconds(86400).useSecureCookie(true);//记住多久

        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true);
    }


    /**
     * 用户名密码校验的Filter
     * @return
     */
    @Bean
    UsernamePasswordAuthenticationFilter MyUsernamePasswordAuthenticationFilter() {
        UsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
        myUsernamePasswordAuthenticationFilter.setPostOnly(true);
        myUsernamePasswordAuthenticationFilter.setUsernameParameter("username");
        myUsernamePasswordAuthenticationFilter.setPasswordParameter("password");
        myUsernamePasswordAuthenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/user/login", "POST"));
        myUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/security/loginsuccess"));
        myUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/security/loginfail"));
        myUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return myUsernamePasswordAuthenticationFilter;
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandler accessDeniedHandler = new MyAccessDeniedHandler();
        return accessDeniedHandler;
    }

    @Bean(name = "accessDecisionManager")
    public AccessDecisionManager accessDecisionManager() {

        MyAccessDecisionManager accessDecisionManager = new MyAccessDecisionManager();
        return accessDecisionManager;
    }

    @Bean(name = "expressionVoter")
    public WebExpressionVoter webExpressionVoter() {
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
        return webExpressionVoter;
    }
    @Bean(name = "expressionHandler")
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        return webSecurityExpressionHandler;
    }
    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() {
        AuthenticationManager authenticationManager = null;
        try {
            authenticationManager = super.authenticationManagerBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authenticationManager;
    }


    /**
     * 加载用户权限
     * @return
     */
    @Bean(name = "myInvocationSecurityMetadataSource")
    public FilterInvocationSecurityMetadataSource myInvocationSecurityMetadataSource() {
        FilterInvocationSecurityMetadataSource myInvocationSecurityMetadataSource = null;
        try {
            myInvocationSecurityMetadataSource = new MyInvocationSecurityMetadataSource(sysPermissionMapper,redisUtils);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return myInvocationSecurityMetadataSource;
    }

}