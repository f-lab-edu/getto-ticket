package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.enums.RedisKey;
import com.flab.gettoticket.repository.RedisCommRepository;
import com.flab.gettoticket.repository.RedisSeatRepository;
import com.flab.gettoticket.repository.SeatRepository;
import com.flab.gettoticket.service.RedisCachingServie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisCachingServieImpl implements RedisCachingServie {

    private final RedisCommRepository redisCommRepository;
    private final RedisSeatRepository redisSeatRepository;
    private final SeatRepository seatRepository;

    @Override
    public void addSeatWarmUpData(long goodsId, long playId) {
        String plainTextKey = String.valueOf(goodsId + ":" + playId);
        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        boolean isExistsKey = redisCommRepository.existsKey(key);

        log.info("isExistsKey: {}", isExistsKey);

        if(!isExistsKey) {
            List<Seat> data = seatRepository.selectSeatList(goodsId, playId);

            log.info("seat db data: {}", data);

            for(Seat seat : data) {
                long playTimeId = seat.getPlayId();
                redisSeatRepository.insertSeatInfo(goodsId, playTimeId, seat);
            }
        }
    }
}
