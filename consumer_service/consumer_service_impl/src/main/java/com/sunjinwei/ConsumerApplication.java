package com.sunjinwei;

import com.sunjinwei.config.CanaryRuleConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @program: com.sunjinwei
 * @author: sun jinwei
 * @create: 2022-05-29 08:45
 * @description:
 **/
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.sunjinwei"})
@LoadBalancerClient(value = "product-service", configuration = CanaryRuleConfiguration.class)
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}