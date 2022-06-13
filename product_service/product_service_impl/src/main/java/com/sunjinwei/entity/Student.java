package com.sunjinwei.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: com.sunjinwei.entity
 * @author: sun jinwei
 * @create: 2022-06-11 22:35
 * @description:
 **/
@Data
public class Student {

    private Long id;

    private String studentName;

    private Integer studentAge;

    private String studentDesc;

    private Date createTime;

    private Date updateTime;

}