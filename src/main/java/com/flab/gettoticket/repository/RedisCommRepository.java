package com.flab.gettoticket.repository;

public interface RedisCommRepository {
    boolean existsKey(String key);
}
