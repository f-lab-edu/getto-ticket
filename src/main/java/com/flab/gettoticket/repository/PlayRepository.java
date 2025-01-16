package com.flab.gettoticket.repository;

import com.flab.gettoticket.dto.SeatCountResponse;
import com.flab.gettoticket.entity.PlayTime;

import java.time.LocalDate;
import java.util.List;

public interface PlayRepository {
    List<String> selectPlayAtList(long goodsId, LocalDate startDate, LocalDate endDate);
    List<PlayTime> selectTimeTableList(long goodsId, LocalDate playAt);
    PlayTime selectTimeTable(long playTimeId, long goodsId);
    List<SeatCountResponse> selectSeatCount(long playTimeId);
}
