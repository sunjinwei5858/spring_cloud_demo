package com.sunjinwei.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @program: com.sunjinwei.config
 * @author: sun jinwei
 * @create: 2022-06-11 22:22
 * @description:
 **/
@Configuration
public class WebClientConfig {

    /**
     * @LoadBalanced 为 WebClient.Build 构造器注入特殊的 Filter，实现负载均衡功能
     * @return
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder register() {
        return WebClient.builder();
    }
}