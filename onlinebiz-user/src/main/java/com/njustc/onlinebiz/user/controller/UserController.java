package com.njustc.onlinebiz.user.controller;

import com.njustc.onlinebiz.user.pojo.User;
import com.njustc.onlinebiz.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserServiceImpl userServiceImpl;

    @RequestMapping("/login")
    public String userLogin(@RequestParam("userName") String userName,
                            @RequestParam("userPassword") String userPassword,
                            HttpServletResponse res){
        User user = userServiceImpl.login(userName, userPassword);
        Cookie cookie = new Cookie("userName", user.getUserName());
        res.addCookie(cookie);
        return "redirect:/user";
    }

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> pv,
                           HttpServletResponse res){
        String userName = pv.get("userName");
        String userPassword = pv.get("userPassword");
        boolean success = userServiceImpl.registerUser(userName, userPassword);
        if (success) {
            return "redirect:/user";
        }
        return "redirect:/register";
    }

}
