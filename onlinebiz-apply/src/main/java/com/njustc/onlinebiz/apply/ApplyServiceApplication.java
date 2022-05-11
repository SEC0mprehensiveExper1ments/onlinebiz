package com.njustc.onlinebiz.apply;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ApplyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplyServiceApplication.class, args);
    }
}
