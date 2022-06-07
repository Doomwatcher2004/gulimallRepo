package com.hyc.gulimall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
 * 1. open-fegin 是声明式远程调用 是客户端发起调用的
 * 2. 声明一个远程调用接口 使用注解 @feignClient 来开启远程调用
 * 3. 主启动类开启设置存放远程调用接口的位置，去扫描位置
 * */
@SpringBootApplication
@MapperScan("com.hyc.gulimall.member.dao")
@EnableDiscoveryClient
@EnableFeignClients("com.hyc.gulimall.member.fegin")
public class GulimallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMemberApplication.class, args);
    }

}
