package com.sjiay.common.security;

import com.alibaba.fastjson.JSON;
import com.sjiay.common.VO.ResultVO;
import com.sjiay.common.Enums.ResultEnum;
import com.sjiay.common.utils.DateUtil;
import com.sjiay.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: zzx
 * @date: 2018/10/16 9:59
 * @description: 登出成功
 */
@Component
@Slf4j
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            final  String authToken = authHeader.substring("Bearer ".length());
            //将token放入黑名单
            redisUtil.hset("blacklist",authToken, DateUtil.getTime());
            log.info("token：{}已加入redis黑名单",authToken);
        }
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.USER_LOGOUT_SUCCESS,true)));
    }
}
