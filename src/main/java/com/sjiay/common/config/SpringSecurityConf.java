package com.sjiay.common.config;

import com.sjiay.common.security.*;
import com.sjiay.common.filters.JwtAuthenticationTokenFilter;
import com.sjiay.permission.service.SelfUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringSecurityConf extends WebSecurityConfigurerAdapter {
    //未登陆时返回 JSON 格式的数据给前端（否则为 html）
    @Autowired
    AjaxAuthenticationEntryPoint authenticationEntryPoint;

    //登录成功返回的 JSON 格式数据给前端（否则为 html）
    @Autowired
    AjaxAuthenticationSuccessHandler authenticationSuccessHandler;

    //登录失败返回的 JSON 格式数据给前端（否则为 html）
    @Autowired
    AjaxAuthenticationFailureHandler authenticationFailureHandler;

    //注销成功返回的 JSON 格式数据给前端（否则为 登录时的 html）
    @Autowired
    AjaxLogoutSuccessHandler logoutSuccessHandler;

    //无权访问返回的 JSON 格式数据给前端（否则为 403 html 页面）
    @Autowired
    AjaxAccessDeniedHandler accessDeniedHandler;

    // 自定义user
    @Autowired
    SelfUserDetailsService userDetailsService;

    // JWT 拦截器
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().
//                passwordEncoder(new MyPasswordEncoder()).
//                withUser("admin").password("123456").roles("ADMIN");
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //去掉CSRF
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 使用 JWT，关闭token
                .and()

                .httpBasic().authenticationEntryPoint(authenticationEntryPoint)

                .and()
                .authorizeRequests()//定义哪些URL需要被保护、哪些不需要被保护
//                .antMatchers("/test/register")//允许匿名注册
//                .permitAll()
                .anyRequest()//任何请求,登录后可以访问
                .access("@rbacauthorityservice.hasPermission(request,authentication)")// RBAC 动态 url 认证

                .and()
                .formLogin()//开启登录, 定义当需要用户登录时候，转到的登录页面
        //        .loginPage("/test/login.html")
        //        .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)//登录成功
                .failureHandler(authenticationFailureHandler)//登录失败
                .permitAll()

                .and()
                .logout()//默认注销行为为logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();
        //记住我
        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService).tokenValiditySeconds(1000);

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);// 无权访问 JSON 格式的数据
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}