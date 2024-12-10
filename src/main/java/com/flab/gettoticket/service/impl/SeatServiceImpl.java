package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.entity.BookingSeat;
import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.entity.Zone;
import com.flab.gettoticket.repository.SeatRepository;
import com.flab.gettoticket.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Override
    public List<Seat> findSeatList(long goodsId) {
        List<Seat> list = seatRepository.selectSeatList(goodsId);

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
