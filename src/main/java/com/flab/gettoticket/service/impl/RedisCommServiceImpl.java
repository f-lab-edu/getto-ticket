package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.repository.RedisCommRepository;
import com.flab.gettoticket.service.RedisCommService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RedisCommServiceImpl implements RedisCommService {

    private final RedisCommRepository redisCommRepository;

    @Override
    public void setData(String key, String value) {
        redisCommRepository.setData(key, value);
    }

    @Override
    public String getData(String key) {
        return redisCommRepository.getData(key);
    }

    @Override
    public void deleteData(String key) {
        redisCommRepository.deleteData(key);
    }

    @Override
    public boolean existsKey(String key) {
        return redisCommRepository.existsKey(key);
    }

    @Override
    public boolean flushAllKey() {
        return redisCommRepository.flushAllKey();
    }
}
