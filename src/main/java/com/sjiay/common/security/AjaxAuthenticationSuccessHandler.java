package com.sjiay.common.security;

import com.alibaba.fastjson.JSON;
import com.sjiay.common.VO.ResultVO;
import com.sjiay.common.utils.AccessAddressUtil;
import com.sjiay.common.utils.JwtTokenUtil;
import com.sjiay.common.Enums.ResultEnum;
import com.sjiay.common.utils.RedisUtil;
import com.sjiay.permission.entity.SelfUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //获取请求的ip地址
        String ip = AccessAddressUtil.getIpAddress(httpServletRequest);
        Map<String,Object> map = new HashMap<>();
        map.put("ip",ip);


        SelfUserDetails userDetails = (SelfUserDetails) authentication.getPrincipal();

        String jwtToken = JwtTokenUtil.generateToken(userDetails.getUsername(), expirationSeconds,map);

        //刷新时间
        Integer expire = validTime*24*60*60*1000;
        //获取请求的ip地址
        String currentIp = AccessAddressUtil.getIpAddress(httpServletRequest);
        redisUtil.setTokenRefresh(jwtToken,userDetails.getUsername(),currentIp);
        httpServletResponse.setCharacterEncoding("utf-8");
        log.info("用户{}登录成功，信息已保存至redis",userDetails.getUsername());
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.USER_LOGIN_SUCCESS,jwtToken,true)));
    }
}
