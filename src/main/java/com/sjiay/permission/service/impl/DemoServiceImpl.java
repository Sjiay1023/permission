package com.sjiay.permission.service.impl;

import com.sjiay.permission.entity.DemoEntity;
import com.sjiay.permission.mapper.DemoMapper;
import com.sjiay.permission.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    private DemoMapper demoMapper;
    @Override
    public List<DemoEntity> getUser() {
        return demoMapper.getUser();
    }

    @Override
    public void register(String username, String password) {
        //因为只是简单注册，故只是对密码加密保存，其他就不添加进来了
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String EncryptedPassword = bCryptPasswordEncoder.encode(password);
        demoMapper.register(username,EncryptedPassword);
    }
}
