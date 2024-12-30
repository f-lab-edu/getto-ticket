package com.flab.gettoticket.repository;

import com.flab.gettoticket.enums.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Slf4j
public class RedisProcessingRepositoryImpl implements RedisProcessingRepository {
    private final RedisTemplate redisTemplate;
    private final HashOperations<String,String,String> hashOperations;

    public RedisProcessingRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Map<String, String> selectProcessingQueueAll(String plainTextKey) {
        String key = RedisKey.PROCESSING_KEY.getKey() + plainTextKey;
        return hashOperations.entries(key);
    }

    @Override
    public long selectProcessingQueueSize(String plainTextKey) {
        String key = RedisKey.PROCESSING_KEY.getKey() + plainTextKey;
        long size = hashOperations.size(key);

        log.info("Processing field 개수 조회 key: {}, size: {}", key, size);

        return size;
    }

    @Override
    public int selectProcessingQueue(String plainTextKey, long userSeq) {
        String key = RedisKey.PROCESSING_KEY.getKey() + plainTextKey;
        String field = String.valueOf(userSeq);
        String value = hashOperations.get(key, field);

        if(value == null) {
            return 0;
        }

        return Integer.parseInt(value);
    }

    @Override
    public boolean hasKey(String plainTextKey, long userSeq) {
        String key = RedisKey.PROCESSING_KEY.getKey() + plainTextKey;
        String field = String.valueOf(userSeq);

        return hashOperations.hasKey(key, field);
    }

    @Override
    public void insertProcessingQueue(String plainTextKey, long userSeq, int status) {
        String key = RedisKey.PROCESSING_KEY.getKey() + plainTextKey;
        String field = String.valueOf(userSeq);

        hashOperations.put(key, field, String.valueOf(status));

        log.info("Processing field 추가 key: {}, field: {}, status: {}", key, field, status);
    }

    @Override
    public void updateProcessingQueue(String plainTextKey, long userSeq, int status) {
        String key = RedisKey.PROCESSING_KEY.getKey() + plainTextKey;
        String field = String.valueOf(userSeq);

        hashOperations.put(key, field, String.valueOf(status));

        log.info("Processing field 수정 key: {}, field: {}, status: {}", key, userSeq, status);
    }

    @Override
    public void removeProcessingQueue(String plainTextKey, long userSeq) {
        String key = RedisKey.PROCESSING_KEY.getKey() + plainTextKey;
        String field = String.valueOf(userSeq);

        hashOperations.delete(key, field);

        log.info("Processing field 삭제 key: {}, field: {}", key, userSeq);
    }
}
