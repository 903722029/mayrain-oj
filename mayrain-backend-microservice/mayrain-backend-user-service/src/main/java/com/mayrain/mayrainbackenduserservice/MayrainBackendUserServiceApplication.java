package com.mayrain.mayrainbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@MapperScan("com.mayrain.mayrainbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.mayrain")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.mayrain.mayrainbackendserviceclient.service"})
public class MayrainBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MayrainBackendUserServiceApplication.class, args);
    }

}
