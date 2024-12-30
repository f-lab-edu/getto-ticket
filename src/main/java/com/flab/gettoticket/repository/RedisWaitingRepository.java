package com.flab.gettoticket.repository;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

public interface RedisWaitingRepository {
    Set<ZSetOperations.TypedTuple<String>> selectRangeByScore(String plainTextKey, long start, long amount);
    long selectWaitingSize(String plainTextKey);
    long selectWaitingRank(String plainTextKey, long userSeq);
    long selectWaitingScore(String plainTextKey, long userSeq);
    List<String> selectPopMinWaitingQueue(String plainTextKey, int count);
    long insertWaitingQueue(String plainTextKey, long userSeq);
    long removeWaitingQueue(String plainTextKey, long userSeq);

}
