package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.user.model.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    public User findUserByUserId(Long userId);

    public User findUserByUserName(String userName);

    // 创建一个新用户，成功返回 true，失败返回 false
    public boolean createUser(String userName, String userPassword);

    public boolean updateUserName(String userName, HttpServletRequest request);

    public boolean updateUserPassword(String oldPassword, String newPassword, HttpServletRequest request);

    // 注销一个用户账号
    public boolean removeUser(HttpServletRequest request);

    // 需要当前请求来操作 session
    public boolean handleLogIn(String username, String password, HttpServletRequest request);

    public boolean handleLogOut(HttpServletRequest request);

    // 检查是否登录
    public boolean checkLogIn(HttpServletRequest request);

    // 获取用户的身份信息，成功返回 User 对象，失败返回 null
    public User getUserIdentity(HttpServletRequest request);

}
