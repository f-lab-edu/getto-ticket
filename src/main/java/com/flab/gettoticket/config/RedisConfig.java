package com.flab.gettoticket.config;

import com.flab.gettoticket.entity.Seat;
import io.lettuce.core.ClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // default: Object <-> Json
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());

        // key: String 형식 직렬화
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // value: Json 형식 직렬화
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));    //new GenericJackson2JsonRedisSerializer()

        // hash key: Hash Field String 형식 직렬화
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // value: Hash 형식 직렬화
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Seat> seatRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Seat> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // key: String 형식 직렬화
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // hash key: Hash Field String 형식 직렬화
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // value 및 hash value: Seat 객체 직렬화
        Jackson2JsonRedisSerializer<Seat> seatSerializer = new Jackson2JsonRedisSerializer<>(Seat.class);
        redisTemplate.setValueSerializer(seatSerializer);
        redisTemplate.setHashValueSerializer(seatSerializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

}