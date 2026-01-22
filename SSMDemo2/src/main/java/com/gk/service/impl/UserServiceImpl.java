package com.gk.service.impl;

import com.gk.mapper.UserMapper;
import com.gk.pojo.User;
import com.gk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public int saveUser(com.gk.pojo.User user) {

        return userMapper.insertUser(user);
    }

    @Override
    public List<User> getUsers() {
        return userMapper.selectAllUsers();
    }


}
