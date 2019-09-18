package com.sjiay.permission.controller;

import com.sjiay.permission.entity.DemoEntity;
import com.sjiay.permission.service.DemoService;
import com.sjiay.common.Enums.ResultEnum;
import com.sjiay.common.VO.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class DemoController {
    @Autowired
    private DemoService demoService;
    @RequestMapping("/getUser")
    public List<DemoEntity> getUser(){
        List<DemoEntity> result = demoService.getUser();
        return result;
    }

    /**
     * 简单注册功能
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/register")
    public Map<String, Object> register(String username, String password){
        demoService.register(username,password);
        return ResultVO.result(ResultEnum.SUCCESS,true);
    }
}
