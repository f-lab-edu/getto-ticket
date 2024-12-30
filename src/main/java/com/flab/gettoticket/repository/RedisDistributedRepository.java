package com.flab.gettoticket.repository;

import java.util.concurrent.TimeUnit;

public interface RedisDistributedRepository {
    boolean tryLock(String resourceKey);
    boolean tryLock(String resourceKey, long waitTime, long leaseTime, TimeUnit timeUnit);
}
