package com.gk.mapper;

import com.gk.pojo.User;

import java.util.List;

public interface UserMapper {

    int insertUser(User user);

    List<User> selectAllUsers();
}
