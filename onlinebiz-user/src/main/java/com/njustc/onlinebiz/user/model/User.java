package com.njustc.onlinebiz.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    // 普通用户，最开始注册账号就是普通用户
    public static final String GUEST_ROLE = "guest";

    // 客户（可能是在普通用户基础上完善一些信息）
    public static final String CUSTOMER_ROLE = "customer";

    // 工作人员
    public static final String EMPLOYEE_ROLE = "employee";

    // 管理员？
    public static final String ADMIN_ROLE = "admin";

    private Long userId;

    private String userName;

    private String userPassword;

    private String userRole;

    public User(String userName, String userPassword, String userRole) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRole = userRole;
    }

}
