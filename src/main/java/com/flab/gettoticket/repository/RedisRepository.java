package com.flab.gettoticket.repository;

import com.flab.gettoticket.entity.Seat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class RedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final int DEFAULT_EXPIRE_SECONDS = 120; //1m

    public void setData(String key, Object value){
        redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }

    public Object getData(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}
