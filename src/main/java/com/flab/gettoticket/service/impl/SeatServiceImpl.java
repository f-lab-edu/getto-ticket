package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.entity.Zone;
import com.flab.gettoticket.repository.RedisSeatRepository;
import com.flab.gettoticket.repository.RedisWaitingRepository;
import com.flab.gettoticket.repository.SeatRepository;
import com.flab.gettoticket.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final RedisWaitingRepository redisWaitingRepository;
    private final RedisSeatRepository redisSeatRepository;

    @Override
    public List<Seat> findSeatList(long goodsId, long playId) {
        String plainTextKey = String.valueOf(goodsId + ":" + playId);
        long waitingSize = redisWaitingRepository.selectWaitingSize(plainTextKey);
        List<Seat> list = new ArrayList<>();

        //대기열이 있다면
        if(waitingSize > 0) {
            list = redisSeatRepository.selectSeatAll(plainTextKey);
            log.info("Seat Caching data");
        }

        if(Objects.isNull(list)) {
            list = seatRepository.selectSeatList(goodsId, playId);
            log.info("Seat Database data");
        }

        if(Objects.isNull(list)) {
            log.error("좌석 정보 조회 중 예외 발생 goodsId: {}", goodsId);
            throw new RuntimeException("좌석 정보 조회에 실패하였습니다.");
        }

        return list;
    }

    @Override
    public List<Zone> findZonePrice(long id) {
        List<Zone> list = seatRepository.selectZonePrice(id);

        if(Objects.isNull(list)) {
            log.error("좌석 가격 조회 중 예외 발생 id: {}", id);
            throw new RuntimeException("좌석 가격 조회에 실패하였습니다.");
        }

        return list;
    }
}
