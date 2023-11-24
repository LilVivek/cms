package com.briup.cms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Bean//这里的RedisTemplate是第三方文件 所以需要配置类里面的bean来重写适合自己的序列化器
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        log.info("自定义RedisTemplate");//这里的序列化是要在外部才能看到的 而不是idea看到的
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //默认的Key序列化器为：JdkSerializationRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());//setKey使用字符串类型的序列化器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());//setValue使用jackson的序列化器

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());//setHashKey使用字符串类型的序列化器
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());//setHashValue使用jackson的序列化器

        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

}
