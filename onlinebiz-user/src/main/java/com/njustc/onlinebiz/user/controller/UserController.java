package com.njustc.onlinebiz.user.controller;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.User;
import com.njustc.onlinebiz.common.model.UserDto;
import com.njustc.onlinebiz.user.service.UserService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> createAccount(
            @RequestParam("userName") String userName,
            @RequestParam("userPassword") String userPassword
    ) {
        if (userService.createUser(userName, userPassword)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    // 登录
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestParam("userName") String userName,
            @RequestParam("userPassword") String userPassword,
            HttpServletRequest request
    ) {
        if (!userService.handleLogIn(userName, userPassword, request)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    // 登出
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        if (userService.handleLogOut(request)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    // 修改用户名
    @PostMapping("/account/username")
    public ResponseEntity<Void> changeUsername(
            @RequestParam("newValue") String userName,
            HttpServletRequest request
    ) {
        // 修改用户名
        if (!userService.updateCurrentUserName(userName, request)) {
            return ResponseEntity.badRequest().build();
        }
        // 成功
        return ResponseEntity.ok().build();
    }

    // 修改密码
    @PostMapping("/account/password")
    public ResponseEntity<Void> changePassword(
            @RequestParam("oldValue") String oldPassword,
            @RequestParam("newValue") String newPassword,
            HttpServletRequest request
    ) {
        if (!userService.updateCurrentUserPassword(oldPassword, newPassword, request)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    // 注销账号
    @DeleteMapping("/account")
    public ResponseEntity<Void> removeIndividualAccount(HttpServletRequest request) {
        if (!userService.removeCurrentUser(request)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    // 获取用户自己的账号信息
    @GetMapping("/account")
    public ResponseEntity<UserDto> getIndividualAccount(HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        // 返回 DTO，这里我们不返回密码，虽然它也是加密的
        return ResponseEntity.ok().body(new UserDto(user));
    }

    // 修改某个账号的角色
    @PostMapping("/account/role")
    public ResponseEntity<Void> changeRole(
            @RequestParam("userName") String userName,
            @RequestParam("newValue") String newValue,
            @RequestParam("userRole") Role userRole
    ) {
        return userService.updateUserRole(userName, newValue, userRole) ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }

    // 获取指定用户ID的账号信息
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Long userId) {
        User user = userService.getUserByUserId(userId);
        return user == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(new UserDto(user));
    }

    // 按用户名查找某个账号
    @GetMapping("/user/search")
    public ResponseEntity<List<UserDto>> searchAccount(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "userRole", required = false) Role userRole
    ) {
        List<User> userList;
        if (userName != null) {
            userList = userService.searchUserByUserName(userName);
        } else if (userRole != null) {
            userList = userService.searchUserByUserRole(userRole);
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(userList.stream().map(UserDto::new).collect(Collectors.toList()));
    }

}
