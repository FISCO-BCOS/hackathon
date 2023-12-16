package com.find.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //设置String类型的key设置序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());

//        //设置String类型的key设置序列化器
//        redisTemplate.setValueSerializer(new StringRedisSerializer());

        //设置Hash类型的key设置序列化器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        //设置redis链接Lettuce工厂
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return redisTemplate;
    }

    /**
     * 配置RedissonClient
     * @return RedissonClient 提供大量的工具方法
     */
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        //此处调用的方法为单节点的redis
        config.useSingleServer().setAddress("redis://10.21.181.3:6379");
        return Redisson.create(config);
    }
}