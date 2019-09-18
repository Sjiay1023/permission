package com.sjiay.permission.mapper;


import com.sjiay.permission.entity.SelfUserDetails;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    //通过username查询用户
    SelfUserDetails getUser(@Param("username")  String username);
}
