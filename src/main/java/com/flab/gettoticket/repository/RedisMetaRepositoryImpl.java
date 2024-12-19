package com.flab.gettoticket.repository;

import com.flab.gettoticket.enums.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Slf4j
public class RedisMetaRepositoryImpl implements RedisMetaRepository{
    private final StringRedisTemplate redisTemplate;
    private final HashOperations<String,String,String> hashOperations;

    public RedisMetaRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Map<String, String> selectQueueMetaInfo(String metaType) {
        String key = RedisKey.QUEUE_METE_KEY.getKey() + metaType;
        return hashOperations.entries(key);
    }

    @Override
    public void insertQueueMetaInfo(String metaType, String queueKey, String metaInfo) {
        String key = RedisKey.QUEUE_METE_KEY.getKey() + metaType;

        hashOperations.put(key, queueKey, metaInfo);
        //hashOperations.put("queue:meta:processing", "processing:goods:100:1:32349810", "공연 ID: 100, 회차 ID: 1, 공연일시: 32349810");

        log.info("Queue Meta Info 추가 key: {}, queueKey: {}, metaInfo: {}", key, queueKey, metaInfo);
    }

    @Override
    public void removeQueueMetaInfo(String metaType, String queueKey) {
        String key = RedisKey.QUEUE_METE_KEY.getKey() + metaType;

        hashOperations.delete(key, queueKey);

        log.info("Queue Meta Info 삭제 key: {}, queueKey: {}", key, queueKey);
    }
}
