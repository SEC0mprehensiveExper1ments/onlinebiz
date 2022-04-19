package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.user.pojo.Administrator;

public interface AdministratorService {
    /**
     * 后台管理人员登录
     *
     * @param administratorName     待登录后台管理人员的用户名
     * @param administratorPassword 待登录后台管理人员的用户密码
     * @return 若登录成功则返回该Administrator对象，否则返回null
     */
    Administrator login(String administratorName, String administratorPassword);
}
