package com.njustc.onlinebiz.user.service.impl;

import com.njustc.onlinebiz.user.mapper.UserMapper;
import com.njustc.onlinebiz.user.pojo.User;
import com.njustc.onlinebiz.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public boolean registerUser(String userName, String userPassword) {
        User user = userMapper.queryUserByUserName(userName);
        if (user == null) {
            userMapper.saveUser(userName, userPassword);
            return true;
        }
        return false;
    }

    @Override
    public User login(String userName, String userPassword) {
        return userMapper.queryUserByUserNameAndUserPassword(userName, userPassword);
    }
}
