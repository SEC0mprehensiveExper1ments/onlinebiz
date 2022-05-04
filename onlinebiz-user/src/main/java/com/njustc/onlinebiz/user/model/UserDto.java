package com.njustc.onlinebiz.user.model;

import com.njustc.onlinebiz.common.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    private Long userId;

    private String userName;

    private String userRole;

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userRole = user.getUserRole();
    }
}
