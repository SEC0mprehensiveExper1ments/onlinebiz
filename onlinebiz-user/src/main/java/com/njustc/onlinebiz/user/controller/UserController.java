package com.njustc.onlinebiz.user.controller;

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
        if (!userService.handleLogOut(request)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
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

    // 验证某个用户名是否存在
    @GetMapping("/user")
    public ResponseEntity<Void> checkAccountByUserName(@RequestParam("userName") String userName) {
        if (userService.findUserByUserName(userName) != null) {
            // 找到返回 ok
            return ResponseEntity.ok().build();
        }
        // 找不到返回 404
        return ResponseEntity.notFound().build();
    }

    // 修改用户名
    @PostMapping("/user/{userId}/username")
    public ResponseEntity<Void> changeUsername(
            @PathVariable(value = "userId") Long userId,
            @RequestParam(value = "newValue") String userName
    ) {
        // 修改用户名
        if (!userService.updateUserName(userId, userName)) {
            return ResponseEntity.badRequest().build();
        }
        // 成功
        return ResponseEntity.ok().build();
    }

    // 修改密码
    @PostMapping("/user/{userId}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable("userId") Long userId,
            @RequestParam("oldValue") String oldPassword,
            @RequestParam("newValue") String newPassword
    ) {
        if (!userService.updateUserPassword(userId, oldPassword, newPassword)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    // 注销账号
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> closeAccount(@PathVariable("userId") Long userId) {
        if (!userService.removeUser(userId)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
