package com.flab.gettoticket.service;

import com.flab.gettoticket.dto.SeatDTO;
import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.model.PlayTime;

import java.time.LocalDate;
import java.util.List;

public interface PlayService {
    SeatDTO findSeatDTO(long goodsId, LocalDate playAt);
    List<LocalDate> findPlayAtList(long goodsId, LocalDate startDate, LocalDate endDate);
    List<SeatCountDTO> findSeatCount(long playTimeId);
    List<PlayTime> findPlayOrder(long goodsId, LocalDate playAt);
}