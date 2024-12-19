package com.flab.gettoticket.repository;

import com.flab.gettoticket.enums.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class RedisWaitingRepositoryImpl implements RedisWaitingRepository{
    private final RedisTemplate<String, String> redisTemplate;
    private ZSetOperations<String, String> zSetOperations;

    public RedisWaitingRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    private final int WAITING_KEY_EXPIRE_DURAITON = 300; //5m, TODO 별도 관리

    @Override
    public Set<ZSetOperations.TypedTuple<String>> selectRangeByScore(String plainTextKey, long start, long amount) {
        String key = RedisKey.WAITING_KEY.getKey() + plainTextKey;

        return zSetOperations.rangeWithScores(key, start, amount);
    }

    @Override
    public long selectWaitingSize(String plainTextKey) {
        String key = RedisKey.WAITING_KEY.getKey() + plainTextKey;
        return zSetOperations.size(key);
    }

    @Override
    public long selectWaitingRank(String plainTextKey, String token) {
        String key = RedisKey.WAITING_KEY.getKey() + plainTextKey;
        String member = token;

        return zSetOperations.rank(key, member);
    }

    @Override
    public long selectWaitingScore(String plainTextKey, String token) {
        String key = RedisKey.WAITING_KEY.getKey() + plainTextKey;
        String member = token;

        double score = zSetOperations.score(key, member);

        return Double.isNaN(score) ? 0L : Math.round(score);
    }

    /**ㅃ
     * score가 작은 것부터 토큰을 조회한다
     * @param plainTextKey
     * @param count
     * @return
     */
    @Override
    public List<String> selectPopMinWaitingQueue(String plainTextKey, int count) {
        String key = RedisKey.WAITING_KEY.getKey() + plainTextKey;
        Set<ZSetOperations.TypedTuple<String>> tupleSet = zSetOperations.popMin(key, count);
        List<String> list = new ArrayList<>();

        if(tupleSet != null) {
            for(ZSetOperations.TypedTuple<String> str : tupleSet) {
                list.add(String.valueOf(str));
            }
        }

        return list;
    }

    @Override
    public long insertWaitingQueue(String plainTextKey, String token) {
        String key = RedisKey.WAITING_KEY.getKey() + plainTextKey;
        long score = System.currentTimeMillis();    //현재 시간을 점수(우선순위)로 사용

        zSetOperations.add(key, token, score);
        redisTemplate.expire(key, WAITING_KEY_EXPIRE_DURAITON, TimeUnit.SECONDS);

        log.info("Waiting Key 추가 key: {}, token: {}, score: {}", key, token, score);

        return score;
    }

    @Override
    public long removeWaitingQueue(String plainTextKey, String token) {
        String key = RedisKey.WAITING_KEY.getKey() + plainTextKey;

        long result = zSetOperations.remove(key, token);

        log.info("Waiting Key 삭제 key: {}, token: {}", key, token);

        return result;
    }
}
