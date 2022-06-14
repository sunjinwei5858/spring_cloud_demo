package com.sunjinwei.controller;

import com.sunjinwei.entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @program: com.sunjinwei.controller
 * @author: sun jinwei
 * @create: 2022-06-11 22:33
 * @description:
 **/
@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/student")
    public Student queryStudent(@RequestHeader("traffic_version") String trafficVersion) {

        System.out.println(" header is===== " + trafficVersion);

        System.out.println("product addStudent invoke......");
        Student student = new Student();
        student.setId(1111L);
        student.setStudentName("alex");
        student.setStudentDesc(trafficVersion);
        student.setStudentAge(20);
        student.setCreateTime(new Date());
        student.setUpdateTime(new Date());
        return student;
    }
}