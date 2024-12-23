package com.flab.gettoticket.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class RedisCommRepositoryImpl implements RedisCommRepository{

    private final RedisTemplate redisTemplate;
    private final RedisConnection redisConnection;

    public RedisCommRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisConnection = redisTemplate.getConnectionFactory().getConnection();
    }

    private final int DEFAULT_EXPIRE_DURATION = 300;

    @Override
    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

}
