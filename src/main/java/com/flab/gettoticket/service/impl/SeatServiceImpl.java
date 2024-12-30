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
        long seatQueueSize = redisSeatRepository.selectSeatQueueSize(plainTextKey);
        List<Seat> list = new ArrayList<>();

        //캐싱된 데이터
        if(seatQueueSize > 0) {
            list = redisSeatRepository.selectSeatAll(plainTextKey);
            log.info("Seat Caching data: {}", list);
        }

        if(list.isEmpty()) {
            list = seatRepository.selectSeatList(goodsId, playId);
            log.info("Seat Database data");
        }

        if(list.isEmpty()) {
            log.error("좌석 정보 조회 중 예외 발생 goodsId: {}", goodsId);
            throw new RuntimeException("좌석 정보 조회에 실패하였습니다.");
        }

        return list;
    }

    @Override
    public Seat findSeat(long goodsId, long playId, long seatId) {
        String plainTextKey = String.valueOf(goodsId + ":" + playId);
        Seat seat = redisSeatRepository.selectSeat(plainTextKey, seatId);
        boolean isCacheHit = true;

        if(seat == null) {
            isCacheHit = false;
            seat = seatRepository.selectSeat(seatId);
        }

        log.info("좌석 캐시 존재 여부 isCacheHit: {}", isCacheHit);

        return seat;
    }

    @Override
    public List<Zone> findZonePrice(long id) {
        List<Zone> list = seatRepository.selectZonePrice(id);

        if(list.isEmpty()) {
            log.error("좌석 가격 조회 중 예외 발생 id: {}", id);
            throw new RuntimeException("좌석 가격 조회에 실패하였습니다.");
        }

        return list;
    }
}
