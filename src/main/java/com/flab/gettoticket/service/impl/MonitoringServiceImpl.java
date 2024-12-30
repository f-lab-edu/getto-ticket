package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.enums.RedisKey;
import com.flab.gettoticket.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonitoringServiceImpl implements MonitoringService {

    private final RedisTemplate redisTemplate;

    @Override
    public boolean createWaitQueue(long goodsId, long playTimeId) {
        String key = RedisKey.TRAFFIC_KEY.getKey() + goodsId + ":playTimeId:" + playTimeId;
        long currentCount = redisTemplate.opsForValue().increment(key);

        //1초마다 트래픽 감지 초기화
        if (currentCount == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(1));
        }

        //임계치 초과 여부 확인
        long threshold = 2; //초당 100건 임계치
        return currentCount > threshold;
    }
}
