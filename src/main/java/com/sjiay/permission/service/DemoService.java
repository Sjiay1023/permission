package com.sjiay.permission.service;

import com.sjiay.permission.entity.DemoEntity;

import java.util.List;

public interface DemoService {
    List<DemoEntity> getUser();
    void register(String username, String password);
}
