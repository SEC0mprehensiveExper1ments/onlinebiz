package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.common.model.Role;
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

    // 启动时创建 root 账号
    @Bean
    public ApplicationRunner createAdmin(UserService userService) {
        return args -> {
            String passwd = UUID.randomUUID().toString();
            if (!userService.createUser("admin", passwd)) {
                log.error("Can not create root!");
                return;
            }
            if (!userService.updateUserRole("admin", Role.ADMIN.toString(), Role.ADMIN)) {
                log.error("Can not change root to admin!");
                return;
            }
            log.info("The initial password for 'admin' is: " + passwd);
        };
    }

}
