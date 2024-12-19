package com.flab.gettoticket.repository;

public interface RedisTransactionRepository {

    //스케줄링용
    void schedulingQueue(String plainTextKey, String metaInfo, long amount);

    //대기열 진입 트랜잭션
    long activateWaitQueue(String plainTextKey, String metaInfo, String token);

    //대기열 -> 처리열 진입 트랜잭션
    void waitToProcessQueue(String plainTextKey, String metaInfo, String token, long queueSize);
}
