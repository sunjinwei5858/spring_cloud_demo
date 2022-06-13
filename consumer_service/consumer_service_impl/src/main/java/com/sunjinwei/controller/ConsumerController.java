package com.sunjinwei.controller;

import com.sunjinwei.entity.Student;
import com.sunjinwei.feign.StudentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @program: com.sunjinwei.controller
 * @author: sun jinwei
 * @create: 2022-06-11 22:24
 * @description:
 **/
@RestController
@RequestMapping("/consume")
public class ConsumerController {

    @Autowired
    private WebClient.Builder webClient;

    @Autowired
    private StudentClient studentClient;

    @GetMapping("/query")
    public Student addStudent() {

        return studentClient.queryStudent();
    }


}