package com.elldev.reactivechat.config;

import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties("redis")
@EnableRedisRepositories
public class RedisConfig {

    private String mode = "single";
    private String password;
    private Integer timeout = 5000;
    private List<String> nodes;

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedissonClient createRedisClient(RedisConfig rc) {
        return RedisClientBuilder.buildRedissonClient(rc);
    }
}
