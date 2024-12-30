package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.entity.PlayTime;
import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.enums.RedisKey;
import com.flab.gettoticket.repository.PlayRepository;
import com.flab.gettoticket.repository.RedisCommRepository;
import com.flab.gettoticket.repository.RedisSeatRepository;
import com.flab.gettoticket.repository.SeatRepository;
import com.flab.gettoticket.service.RedisCachingServie;
import com.flab.gettoticket.util.TimeUnitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisCachingServieImpl implements RedisCachingServie {

    private final RedisCommRepository redisCommRepository;
    private final RedisSeatRepository redisSeatRepository;
    private final SeatRepository seatRepository;
    private final PlayRepository playRepository;
    private final RedisTemplate<String, Seat> redisTemplate;

    @Override
    public void addSeatWarmUpData(long goodsId, long playId) {
        String plainTextKey = String.valueOf(goodsId + ":" + playId);
        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;

        boolean isExistsKey = redisCommRepository.existsKey(key);

        log.info("isExistsKey: {}", isExistsKey);

        if(!isExistsKey) {
            List<Seat> data = seatRepository.selectSeatList(goodsId, playId);
            PlayTime playTime = playRepository.selectTimeTable(playId, goodsId);

            if(playTime.getPlayAt() == null) {
                log.error("조회된 공연 정보가 없습니다. goodsId: {}, playId: {}", goodsId, playId);
            }
            else {
                LocalDate playAt = playTime.getPlayAt();
                long playDateTime = TimeUnitUtil.getLocalDateToMillSec(playAt);
                long keyExpireDuration = TimeUnitUtil.getCurrDateTimeDiff(playDateTime);      //'playDateTime - 현재 시간' 까지 key 유효시간 설정

                log.info("seat db data: {}", data);

                for(Seat seat : data) {
                    redisSeatRepository.insertSeatInfo(plainTextKey, seat);
                }

                redisTemplate.expire(key, keyExpireDuration, TimeUnit.SECONDS);
            }
        }
    }
}
