package com.flab.gettoticket.service;

public interface RedisCommService {
    void setData(String key, String value);
    String getData(String key);
    void deleteData(String key);
    boolean existsKey(String key);
    boolean flushAllKey();

}
