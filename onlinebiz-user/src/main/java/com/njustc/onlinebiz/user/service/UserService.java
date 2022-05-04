package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    // 创建一个新用户，成功返回 true，失败返回 false
    boolean createUser(String userName, String userPassword);

    // 获取用户的身份信息，成功返回 User 对象，失败返回 null
    User getCurrentUser(HttpServletRequest request);

    boolean updateCurrentUserName(String userName, HttpServletRequest request);

    boolean updateCurrentUserPassword(String oldPassword, String newPassword, HttpServletRequest request);

    // 注销一个用户账号
    boolean removeCurrentUser(HttpServletRequest request);

    // 处理登录，需要 request 来操作 session
    boolean handleLogIn(String username, String password, HttpServletRequest request);

    boolean handleLogOut(HttpServletRequest request);

    // 检查是否登录
    boolean checkLogIn(HttpServletRequest request);

    // 根据用户名搜索用户账号
    List<User> searchUserByUserName(String userName);

    // 修改某个用户的角色
    boolean updateUserRole(String userName, String userRole);
}
