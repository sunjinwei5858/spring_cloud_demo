package com.sunjinwei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: com.sunjinwei
 * @author: sun jinwei
 * @create: 2022-05-29 08:45
 * @description:
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class ProductApplication222 {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication222.class, args);
    }
}