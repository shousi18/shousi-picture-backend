package com.shousi.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class RedisConfig {

    private final RedisConnectionFactory connectionFactory;

    public RedisConfig(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @PostConstruct
    public void init() {
        try {
            // 测试Redis连接
            connectionFactory.getConnection().ping();
            log.info("Redis连接成功");
        } catch (Exception e) {
            log.error("Redis连接失败，请检查Redis服务是否启动或配置是否正确: {}", e.getMessage());
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        // 使用GenericJackson2JsonRedisSerializer来序列化和反序列化redis的value值
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}