package com.njustc.onlinebiz.user.controller;

import com.njustc.onlinebiz.user.model.User;
import com.njustc.onlinebiz.user.model.UserDto;
import com.njustc.onlinebiz.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    // 注册账号
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestParam("userName") String userName,
            @RequestParam("userPassword") String userPassword
    ) {
        if (!userService.createUser(userName, userPassword)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    // 查看当前登录状态（先留着，后面不用的话再说）
    @GetMapping("/login/status")
    public ResponseEntity<Void> getLogInStatus(HttpServletRequest request) {
        return userService.checkLogIn(request) ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 验证某个账号是否存在
    @GetMapping("/account/existence")
    public ResponseEntity<Void> checkAccountByUserName(@RequestParam("userName") String userName) {
        if (userService.findUserByUserName(userName) != null) {
            // 找到返回 ok
            return ResponseEntity.ok().build();
        }
        // 找不到返回 404
        return ResponseEntity.notFound().build();
    }

    // 修改用户名
    @PostMapping("/account/username")
    public ResponseEntity<Void> changeUsername(
            @RequestParam(value = "newValue") String userName,
            HttpServletRequest request
    ) {
        // 修改用户名
        if (!userService.updateUserName(userName, request)) {
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
        if (!userService.updateUserPassword(oldPassword, newPassword, request)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    // 注销账号
    @DeleteMapping("/account")
    public ResponseEntity<Void> closeAccount(HttpServletRequest request) {
        if (!userService.removeUser(request)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    // 获取用户自己的账号信息
    @GetMapping("/account")
    public ResponseEntity<UserDto> getUserIdentity(HttpServletRequest request) {
        User user = userService.getUserIdentity(request);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        // 返回 DTO，这里我们不返回密码，虽然它也是加密的
        return ResponseEntity.ok().body(new UserDto(user));
    }
}
