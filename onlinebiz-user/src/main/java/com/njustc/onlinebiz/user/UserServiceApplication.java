package com.njustc.onlinebiz.user;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@Slf4j
@EnableCaching
@EnableEurekaClient
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    // 启动时创建 root 账号
    @Bean
    public ApplicationRunner createAdmin(UserService userService) {
        return args -> {
            String passwd = UUID.randomUUID().toString();
            if (!userService.createUser("root", passwd)) {
                log.error("Can not create root!");
                return;
            }
            if (!userService.updateUserRole("root", Role.ADMIN)) {
                log.error("Can not change root to admin!");
                return;
            }
            log.info("The initial password for 'root' is: " + passwd);
        };
    }

}
