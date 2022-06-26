package com.njustc.onlinebiz.sample.configuration;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableDiscoveryClient
public class SampleServiceConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() { return new RestTemplate(); }
}

