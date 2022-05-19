package com.njustc.onlinebiz.user.configuration;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Slf4j
@Configuration
public class UserConfiguration {

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
