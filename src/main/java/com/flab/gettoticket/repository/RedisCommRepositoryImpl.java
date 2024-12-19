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

    private final int DEFAULT_EXPIRE_DURATION = 300; //5m

    @Override
    public void setData(String key, String value){
        redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public String getData(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteData(String key){
        redisTemplate.delete(key);
    }

    @Override
    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean flushAllKey() {
        boolean result = true;

        try {
            redisConnection.serverCommands().flushAll();    //return void
        } catch(Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }
}
