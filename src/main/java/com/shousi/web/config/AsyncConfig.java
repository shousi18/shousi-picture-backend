package com.shousi.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务
 */
@Configuration
public class AsyncConfig {

    @Bean(name = "pictureExecutor")
    public Executor pictureExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3); // 核心线程数
        executor.setMaxPoolSize(5); // 最大线程数
        executor.setQueueCapacity(100); // 队列容量
        executor.setThreadNamePrefix("Picture-Thread-"); // 线程名前缀
        executor.setKeepAliveSeconds(60); // 空闲线程存活时间
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
    
    @Bean(name = "taskExecutor")
    public ExecutorService taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 核心线程数
        executor.setMaxPoolSize(10); // 最大线程数
        executor.setQueueCapacity(100); // 队列容量
        executor.setThreadNamePrefix("Thumb-Thread-"); // 线程名前缀
        executor.setKeepAliveSeconds(60); // 空闲线程存活时间
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }
}