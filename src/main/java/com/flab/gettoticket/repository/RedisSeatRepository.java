package com.flab.gettoticket.repository;

import com.flab.gettoticket.entity.Seat;

import java.util.List;

public interface RedisSeatRepository {
    List<Seat> selectSeatAll(String plainTextKey);
    long selectSeatQueueSize(String plainTextKey);
    String selectSaleYn(String key, long seatId);
    void insertSeatInfo(long goodsId, long playTimeId, Seat seat);
    void deleteSeatInfo(String plainTextKey, long seatId);
}
