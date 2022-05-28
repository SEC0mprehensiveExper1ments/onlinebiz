package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.user.model.User;
import com.njustc.onlinebiz.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Slf4j
@Configuration
public class UserServiceConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate =  new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    // 启动时创建 admin 账号
    @Bean
    public ApplicationRunner createAdmin(UserService userService, UserMapper userMapper) {
        return args -> {
            // 判断 admin 是否已经存在，如果存在就删掉原来的账号
            User user = userMapper.selectUserByUserName("admin");
            if (user != null) {
                userMapper.deleteUserById(user.getUserId());
            }
            // 注册新的 admin 账号
            String passwd = UUID.randomUUID().toString();
            try {
                userService.createUser("admin", passwd);
                userService.updateUserRole("admin", Role.ADMIN.toString(), Role.ADMIN);
            } catch (Exception e) {
                log.error("Failed to create admin: " + e.getMessage());
            }
            log.info("The initial password for 'admin' is: " + passwd);
        };
    }

}
