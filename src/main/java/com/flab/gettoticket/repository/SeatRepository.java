package com.flab.gettoticket.repository;

import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.entity.Zone;

import java.util.List;

public interface SeatRepository {
    List<Seat> selectSeatList(long goodsId, long playId);
    Seat selectSeat(long seatId);
    List<Zone> selectZonePrice(long id);
    int updateSeatStatusCode(long id, int statusCode);
}
