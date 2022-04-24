package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.user.pojo.Tester;

public interface TesterService {
    /**
     * 测试人员登录
     *
     * @param testerName     待登录测试人员的用户名
     * @param testerPassword 待登录测试人员的用户密码
     * @return 若登录成功则返回该Tester对象，否则返回null
     */
    Tester login(String testerName, String testerPassword);
}
