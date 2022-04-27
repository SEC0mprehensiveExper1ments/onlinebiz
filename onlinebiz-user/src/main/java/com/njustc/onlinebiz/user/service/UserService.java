package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.user.model.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    User findUserByUserId(Long userId);

    User findUserByUserName(String userName);

    // 创建一个新用户，成功返回 true，失败返回 false
    boolean createUser(String userName, String userPassword);

    boolean updateUserName(String userName, HttpServletRequest request);

    boolean updateUserPassword(String oldPassword, String newPassword, HttpServletRequest request);

    // 注销一个用户账号
    boolean removeUser(HttpServletRequest request);

    // 需要当前请求来操作 session
    boolean handleLogIn(String username, String password, HttpServletRequest request);

    boolean handleLogOut(HttpServletRequest request);

    // 检查是否登录
    boolean checkLogIn(HttpServletRequest request);

    // 获取用户的身份信息，成功返回 User 对象，失败返回 null
    User getUserIdentity(HttpServletRequest request);

}
