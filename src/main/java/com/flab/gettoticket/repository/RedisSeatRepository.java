package com.flab.gettoticket.repository;

import com.flab.gettoticket.entity.Seat;

import java.util.List;

public interface RedisSeatRepository {
    List<Seat> selectSeatAll(String plainTextKey);
    Seat selectSeat(String key, long seatId);
    long selectSeatQueueSize(String plainTextKey);
    void insertSeatInfo(String plainTextKey, Seat seat);
    void deleteSeatInfo(String plainTextKey, long seatId);
}
