package com.flab.gettoticket.repository;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

public interface RedisWaitingRepository {
    Set<ZSetOperations.TypedTuple<String>> selectRangeByScore(String plainTextKey, long start, long amount);
    long selectWaitingSize(String plainTextKey);
    long selectWaitingRank(String plainTextKey, String token);
    long selectWaitingScore(String plainTextKey, String token);
    List<String> selectPopMinWaitingQueue(String plainTextKey, int count);
    long insertWaitingQueue(String plainTextKey, String token);
    long removeWaitingQueue(String plainTextKey, String token);

}
