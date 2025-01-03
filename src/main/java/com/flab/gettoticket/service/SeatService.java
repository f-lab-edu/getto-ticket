package com.flab.gettoticket.service;

import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.entity.Zone;

import java.util.List;

public interface SeatService {
    List<Seat> findSeatList(long goodsId, long playId);
    Seat findSeat(long goodsId, long playId, long seatId);
    List<Zone> findZonePrice(long id);
}
