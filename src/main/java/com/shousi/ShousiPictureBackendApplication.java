package com.shousi;

import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication(exclude = {ShardingSphereAutoConfiguration.class})
@EnableAsync
@MapperScan("com.shousi.web.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableWebSocket
public class ShousiPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShousiPictureBackendApplication.class, args);
    }

}
