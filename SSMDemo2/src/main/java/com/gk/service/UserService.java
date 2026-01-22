package com.gk.service;

import com.gk.pojo.User;

import java.util.List;

public interface UserService {
    int saveUser(User user);

    List<User> getUsers();
}
