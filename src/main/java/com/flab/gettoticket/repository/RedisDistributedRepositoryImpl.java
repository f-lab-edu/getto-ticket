package com.flab.gettoticket.repository;

import com.flab.gettoticket.exception.lock.DistributedIllegalMonitorStateException;
import com.flab.gettoticket.exception.lock.DistributedInterruptedException;
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
    public boolean tryLock(String resourceKey) {
        boolean available = false;
        String lockName = LOCK_NAME_PREFIX + resourceKey;
        RLock rLock = redissonClient.getLock(lockName);

        try {
            available = rLock.tryLock(WAIT_TIME, LEASE_TIME, TIME_UNTIT);
            log.info("tryLock 완료: {}", rLock.getName());
        } catch (InterruptedException e) {
            log.info("catch exception - lock name: {}", rLock.getName());
            throw new DistributedInterruptedException("lock 획득에 실패했습니다.");
        } finally{
            try{
                rLock.unlock();
                log.info("unlock 완료: {}", rLock.getName());
            } catch (IllegalMonitorStateException e) {
                log.info("이미 unlock 처리된 lock 입니다. lock name: {}", rLock.getName());
            }
        }

        return available;
    }

    @Override
    public boolean tryLock(String resourceKey, long waitTime, long leaseTime, TimeUnit timeUnit) {
        boolean available = false;
        String lockName = LOCK_NAME_PREFIX + resourceKey;
        RLock rLock = redissonClient.getLock(lockName);

        try {
            available = rLock.tryLock(waitTime, leaseTime, timeUnit);
            log.info("tryLock 완료: {}", rLock.getName());
        } catch (InterruptedException e) {
            log.info("catch exception - lock name: {}", rLock.getName());
            throw new DistributedInterruptedException("lock 획득에 실패했습니다.");
        } finally{
            try{
                rLock.unlock();
                log.info("unlock 완료: {}", rLock.getName());
            } catch (IllegalMonitorStateException e) {
                log.info("이미 unlock 처리된 lock 입니다. lock name: {}", rLock.getName());
            }
        }

        return available;
    }
}
