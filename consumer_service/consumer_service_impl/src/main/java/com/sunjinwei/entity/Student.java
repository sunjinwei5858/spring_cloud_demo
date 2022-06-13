package com.sunjinwei.entity;

import lombok.Data;

/**
 * @program: com.sunjinwei.entity
 * @author: sun jinwei
 * @create: 2022-06-11 22:28
 * @description:
 **/
@Data
public class Student {

    private Long id;

    private String studentName;

    private Integer studentAge;

    private String studentDesc;

}