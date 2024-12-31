package com.flab.gettoticket.repository;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class RedisDistributedRepositoryImpl implements RedisDistributedRepository{
    private final RedissonClient redissonClient;

    public RedisDistributedRepositoryImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private final String LOCK_NAME_PREFIX = "lock:";
    private final long WAIT_TIME = 5L;  //락 획득 대기시간
    private final long LEASE_TIME = 2L; //락 점유시간
    private final TimeUnit TIME_UNTIT = TimeUnit.SECONDS;

    @Override
    public boolean tryLock(String resourceKey) throws InterruptedException {
        return tryWithParams(resourceKey, WAIT_TIME, LEASE_TIME, TIME_UNTIT);
    }

    @Override
    public boolean tryLock(String resourceKey, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
        return tryWithParams(resourceKey, waitTime, leaseTime, timeUnit);
    }

    private boolean tryWithParams(String resourceKey, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
        boolean available = false;
        String lockName = LOCK_NAME_PREFIX + resourceKey;
        RLock rLock = redissonClient.getLock(lockName);

        try {
            available = rLock.tryLock(waitTime, leaseTime, timeUnit);
            log.info("tryLock 완료: {}", rLock.getName());
        } finally {
            safelyUnlock(rLock);
        }

        return available;
    }

    private void safelyUnlock(RLock rLock) throws IllegalMonitorStateException {
        try {
            if (rLock.isHeldByCurrentThread()) {    //락 점유 여부 확인
                rLock.unlock();
                log.info("unlock 완료: {}", rLock.getName());
            }
        } finally {}
    }
}
