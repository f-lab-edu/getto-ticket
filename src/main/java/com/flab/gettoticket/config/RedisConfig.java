package com.flab.gettoticket.config;

import io.lettuce.core.ClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key와 Value에 대해 각각의 Serializer 설정
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());  //new Jackson2JsonRedisSerializer<>(ObjectDto.class)

        // Hash Key와 Hash Value의 Serializer 설정
        template.setHashKeySerializer(new StringRedisSerializer());  // Hash의 Key는 일반 문자열이므로 StringRedisSerializer
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());  // Hash의 Value를 직렬화할 Serializer

        // ZSet용 Value Serializer
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();

        return template;
    }

}