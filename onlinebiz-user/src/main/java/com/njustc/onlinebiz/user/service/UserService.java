package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.user.pojo.User;

public interface UserService {
  /**
   * 注册用户
   *
   * @param userName     待注册用户的用户名
   * @param userPassword 待注册用户的用户密码
   * @return 若注册成功返回true，否则返回false
   */
  boolean registerUser(String userName, String userPassword);

  /**
   * 用户登录
   *
   * @param userName     待登录用户的用户名
   * @param userPassword 待登录用户的用户密码
   * @return 若登录成功则返回该User对象，否则返回null
   */
  User login(String userName, String userPassword);
}

