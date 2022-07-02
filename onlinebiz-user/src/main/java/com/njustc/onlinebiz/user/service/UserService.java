package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务的接口类
 */
public interface UserService {

    /**
     * 创建一个新用户
     * @param userName 用户名
     * @param userPassword 用户密码
     */
    void createUser(String userName, String userPassword);

    /**
     * 获取当前登录的用户的信息
     * @param request 发来的请求对象
     * @return 如果 request 标志的用户目前处于登录状态，则返回该用户对象；否则返回 null
     */
    User getCurrentUser(HttpServletRequest request);

    /**
     * 更新当前登录的用户的用户名
     * @param userName 更新后的用户名
     * @param request 对应的 Http 请求
     */
    void updateCurrentUserName(String userName, HttpServletRequest request);

    /**
     * 更新当前登录的用户的用户密码
     * @param oldPassword 旧的用户密码
     * @param newPassword 新的用户密码
     * @param request 对应的 Http 请求
     */
    void updateCurrentUserPassword(String oldPassword, String newPassword, HttpServletRequest request);

    /**
     * 注销当前登录的用户账号
     * @param request 对应的 Http 请求
     */
    void removeCurrentUser(HttpServletRequest request);

    /**
     * 处理用户登录请求
     * @param username 用户名
     * @param password 用户密码
     * @param request 对应的 Http 请求
     */
    void handleLogIn(String username, String password, HttpServletRequest request);

    /**
     * 处理用户登出请求
     * @param request 对应的 Http 请求
     */
    void handleLogOut(HttpServletRequest request);

    /**
     * 根据用户名搜索用户账号
     * @param userName 搜索关键字
     * @return 匹配的用户列表
     */
    List<User> searchUserByUserName(String userName);

    /**
     * 根据用户角色查询用户
     * @param userRole 要查询的用户角色
     * @return 匹配的用户列表
     */
    List<User> searchUserByUserRole(Role userRole);

    /**
     * 修改某个用户的角色
     * @param userName 要修改的用户名
     * @param newValue 新的用户角色
     * @param userRole 执行此操作的用户角色
     */
    void updateUserRole(String userName, String newValue, Role userRole);

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 如果存在返回该用户对象，否则返回 null
     */
    User getUserByUserId(Long userId);

}
