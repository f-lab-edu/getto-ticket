package com.flab.gettoticket.repository;

import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.model.PlayTime;

import java.time.LocalDate;
import java.util.List;

public interface PlayRepository {
    List<String> selectPlayAtList(long goodsId, LocalDate startDate, LocalDate endDate);
    List<PlayTime> selectTimeTable(long goodsId, LocalDate playAt);
    List<SeatCountDTO> selectSeatCount(long playTimeId);
}
