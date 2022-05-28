package com.njustc.onlinebiz.user.model;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private Long userId;

    private String userName;

    private String userPassword;

    private Role userRole;

    public User(String userName, String userPassword, Role userRole) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRole = userRole;
    }

    public UserDto toUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setUserName(userName);
        userDto.setUserRole(userRole);
        return userDto;
    }

}
