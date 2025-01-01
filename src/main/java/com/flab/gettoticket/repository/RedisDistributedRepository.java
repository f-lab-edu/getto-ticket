package com.flab.gettoticket.repository;

import java.util.concurrent.TimeUnit;

public interface RedisDistributedRepository {
    boolean tryLock(String resourceKey) throws InterruptedException;
    boolean tryLock(String resourceKey, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException;
}
