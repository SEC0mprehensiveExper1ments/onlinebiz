package com.njustc.onlinebiz.doc.configuration;


import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableDiscoveryClient
public class DocConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() { return new RestTemplate();}
}
