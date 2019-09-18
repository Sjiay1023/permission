package com.sjiay.permission.mapper;

import com.sjiay.permission.entity.DemoEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemoMapper {
    List<DemoEntity> getUser();

    void register(@Param("username") String username, @Param("password") String password);

}
