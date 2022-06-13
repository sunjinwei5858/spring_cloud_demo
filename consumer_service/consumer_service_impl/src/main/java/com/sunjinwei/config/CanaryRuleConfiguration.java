package com.sunjinwei.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @program: com.sunjinwei.config
 * @author: sun jinwei
 * @create: 2022-06-09 06:54
 * @description:
 **/
// 注意这里不要写上@Configuration注解 因为不需要应用到全局
// 在启动类将@LoadBalancerClient注解和配置类和服务进行关联起来
public class CanaryRuleConfiguration {

    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorLoadBalancer(
            Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory
    ) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new CanaryRule(
                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
                name
        );
    }

}