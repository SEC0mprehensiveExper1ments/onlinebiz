package com.njustc.onlinebiz.user.controller;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.user.model.User;
import com.njustc.onlinebiz.common.model.UserDto;
import com.njustc.onlinebiz.user.exception.UserInvalidArgumentException;
import com.njustc.onlinebiz.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 注册账号
    @PostMapping("/register")
    public void createAccount(
            @RequestParam("userName") String userName,
            @RequestParam("userPassword") String userPassword
    ) {
        userService.createUser(userName, userPassword);
    }

    // 登录
    @PostMapping("/login")
    public void login(
            @RequestParam("userName") String userName,
            @RequestParam("userPassword") String userPassword,
            HttpServletRequest request
    ) {
        userService.handleLogIn(userName, userPassword, request);
    }

    // 登出
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        userService.handleLogOut(request);
    }

    // 修改用户名
    @PostMapping("/account/username")
    public void changeUsername(
            @RequestParam("newValue") String userName,
            HttpServletRequest request
    ) {
        // 修改用户名
        userService.updateCurrentUserName(userName, request);
    }

    // 修改密码
    @PostMapping("/account/password")
    public void changePassword(
            @RequestParam("oldValue") String oldPassword,
            @RequestParam("newValue") String newPassword,
            HttpServletRequest request
    ) {
        userService.updateCurrentUserPassword(oldPassword, newPassword, request);
    }

    // 注销账号
    @DeleteMapping("/account")
    public void removeIndividualAccount(HttpServletRequest request) {
        userService.removeCurrentUser(request);
    }

    // 获取用户自己的账号信息
    @GetMapping("/account")
    public UserDto getIndividualAccount(HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        if (user != null) {
            // 返回 DTO，这里我们不返回密码，虽然它也是加密的
            return user.toUserDto();
        }
        return null;
    }

    // 修改某个账号的角色
    @PostMapping("/account/role")
    public void changeRole(
            @RequestParam("userName") String userName,
            @RequestParam("newValue") String newValue,
            @RequestParam("userRole") Role userRole
    ) {
        userService.updateUserRole(userName, newValue, userRole);
    }

    // 获取指定用户ID的账号信息
    @GetMapping("/user/{userId}")
    public UserDto getUser(@PathVariable("userId") Long userId) {
        User user = userService.getUserByUserId(userId);
        if (user != null) {
            return user.toUserDto();
        }
        return null;
    }

    // 按用户名查找某个账号
    @GetMapping("/user/search")
    public List<UserDto> searchAccount(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "userRole", required = false) Role userRole
    ) {
        List<User> userList;
        if (userName != null) {
            userList = userService.searchUserByUserName(userName);
        } else if (userRole != null) {
            userList = userService.searchUserByUserRole(userRole);
        } else {
            throw new UserInvalidArgumentException("搜索条件不能全空");
        }
        return userList.stream().map(User::toUserDto).collect(Collectors.toList());
    }

}
