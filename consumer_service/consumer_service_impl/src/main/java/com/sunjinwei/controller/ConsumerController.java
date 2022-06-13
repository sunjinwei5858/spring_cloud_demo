package com.sunjinwei.controller;

import com.sunjinwei.constant.ConsumerConstant;
import com.sunjinwei.entity.Student;
import com.sunjinwei.feign.StudentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private StudentClient studentClient;

    @GetMapping("/query")
    public Student addStudent(@RequestHeader(ConsumerConstant.TRAFFIC_VERSION) String trafficVersion) {

        return studentClient.queryStudent(trafficVersion);
    }


}