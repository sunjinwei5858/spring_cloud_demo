package com.sunjinwei.config;

import com.alibaba.nacos.common.utils.StringUtils;
import com.sunjinwei.constant.ConsumerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @program: com.sunjinwei.config.loadbalance
 * @author: sun jinwei
 * @create: 2022-06-09 06:55
 * @description: 自定义负载均衡策略 可以单独拿出来作为一个公共组件
 * CanaryRule 如何识别测试流量？
 * CanaryRule 如何对测试流量做负载均衡
 **/
@Slf4j
public class CanaryRule implements ReactorServiceInstanceLoadBalancer {

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers;
    private String serviceId;

    // 定义一个轮训策略的种子
    final AtomicInteger position;

    public CanaryRule(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers, String serviceId) {
        this.serviceInstanceListSuppliers = serviceInstanceListSuppliers;
        this.serviceId = serviceId;
        position = new AtomicInteger(new Random().nextInt(1000));
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {

        ServiceInstanceListSupplier supplier = serviceInstanceListSuppliers.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(
                serviceInstances -> processInstanceResponse(supplier, serviceInstances, request)
        );
    }

    private Response<ServiceInstance> processInstanceResponse(
            ServiceInstanceListSupplier supplier,
            List<ServiceInstance> serviceInstances,
            Request request
    ) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances, request);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> serviceInstances, Request request) {
        // 注册中心无实例 抛出异常
        if (CollectionUtils.isEmpty(serviceInstances)) {
            log.warn("No instance available {}", serviceId);
            return new EmptyResponse();
        }
        // 从请求header中获取特定的流量打标值
        // 注意 以下代码仅仅适用于webclient调用，如果使用resttemplate或者feign则需要额外适配
        DefaultRequestContext context = (DefaultRequestContext) request.getContext();
        RequestData requestData = (RequestData) context.getClientRequest();
        HttpHeaders headers = requestData.getHeaders();
        String trafficVersion = headers.getFirst(ConsumerConstant.TRAFFIC_VERSION);


        // 通过元数据可以获取 集群的信息
        serviceInstances.stream().forEach(it -> {
            Map<String, String> metadata = it.getMetadata();
            String clusterName = metadata.get("nacos.cluster");
            System.out.println("======" + clusterName);
        });

        // 如果没有找到打标标记或者为空 则使用RoundRobin规则进行轮训
        if (StringUtils.isBlank(trafficVersion)) {
            // 过滤掉所有金丝雀测试的节点 Medata有值的节点
            List<ServiceInstance> noneInstances = serviceInstances.stream().filter(
                    e -> !e.getMetadata().containsKey(ConsumerConstant.TRAFFIC_VERSION)
            ).collect(Collectors.toList());
            return getRoundRobinInstance(noneInstances);
        }
        // 否则 找出所有金丝雀服务器 使用roundrobin算法调一台
        List<ServiceInstance> canaryInstances = serviceInstances.stream().filter(
                e -> {
                    String trafficVersionInMetadata = e.getMetadata().get(ConsumerConstant.TRAFFIC_VERSION);
                    return StringUtils.equalsIgnoreCase(trafficVersionInMetadata, trafficVersion);
                }

        ).collect(Collectors.toList());
        return getRoundRobinInstance(canaryInstances);
    }

    /**
     * 轮询机制获取节点
     *
     * @param instances
     * @return
     */
    private Response<ServiceInstance> getRoundRobinInstance(List<ServiceInstance> instances) {
        // 如果没有可用节点 则返回空
        if (instances.isEmpty()) {
            log.info("instance is empty....");
            return new EmptyResponse();
        }
        int pos = Math.abs(this.position.incrementAndGet());
        ServiceInstance serviceInstance = instances.get(pos % instances.size());
        return new DefaultResponse(serviceInstance);
    }
}