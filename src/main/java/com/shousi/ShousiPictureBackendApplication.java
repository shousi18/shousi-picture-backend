package com.shousi;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.shousi.web.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
public class ShousiPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShousiPictureBackendApplication.class, args);
    }

}
