package com.flab.gettoticket.repository;

import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.entity.Zone;

import java.util.List;

public interface SeatRepository {
    List<Seat> selectSeatList(long goodsId, long playId);
    List<Zone> selectZonePrice(long id);
    int updateSeatSaleYn(long id, String saleYn);
}
