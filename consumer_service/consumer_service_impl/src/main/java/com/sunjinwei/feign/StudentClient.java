package com.sunjinwei.feign;

import com.sunjinwei.constant.ConsumerConstant;
import com.sunjinwei.entity.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @program: com.sunjinwei.feign
 * @author: sun jinwei
 * @create: 2022-06-13 07:58
 * @description:
 **/
@FeignClient(value = "product-service", path = "/product")
public interface StudentClient {

    @GetMapping("/student")
    Student queryStudent(@RequestHeader(ConsumerConstant.TRAFFIC_VERSION) String trafficVersion);
}
