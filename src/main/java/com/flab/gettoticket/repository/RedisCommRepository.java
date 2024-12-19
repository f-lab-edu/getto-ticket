package com.flab.gettoticket.repository;

public interface RedisCommRepository {
    void setData(String key, String value);
    String getData(String key);
    void deleteData(String key);
    boolean existsKey(String key);
    boolean flushAllKey();
}
