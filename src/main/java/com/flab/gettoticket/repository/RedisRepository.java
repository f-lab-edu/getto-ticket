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

@Repository
@Slf4j
public class RedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ZSetOperations<String, Object> zsetOperations;

    public RedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zsetOperations = redisTemplate.opsForZSet();
    }

    private final int DEFAULT_EXPIRE_SECONDS = 120; //1m

    public Object findObjectByScore(String key, int start, int amount) {
        List<Object> list = new ArrayList<>();

        Set<ZSetOperations.TypedTuple<Object>> tupleSet = zsetOperations.rangeByScoreWithScores(key, start, amount);

        if(tupleSet != null) {
            for(ZSetOperations.TypedTuple<Object> obj : tupleSet) {
                try {
                    Object object = obj.getValue();

                    if(object != null) {
                        list.add(object);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            log.info("keys is null");
        }

        return list.isEmpty() ? list : list.get(0);
    }

    public void saveObject(Object object, String key, double score) {
        Set<ZSetOperations.TypedTuple<Object>> tupleSet = new HashSet<>();

        tupleSet.add(new DefaultTypedTuple<>(object, score));
        zsetOperations.add(key, tupleSet);
//        redisTemplate.expire(key, Duration.ofSeconds(DEFAULT_EXPIRE_SECONDS));
    }
}
