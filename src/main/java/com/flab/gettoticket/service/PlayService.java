package com.flab.gettoticket.service;

import com.flab.gettoticket.dto.PlayTimeDTO;
import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.entity.PlayTime;

import java.time.LocalDate;
import java.util.List;

public interface PlayService {
    List<String> findPlayAtList(long goodsId, LocalDate startDate, LocalDate endDate);
    List<PlayTime> findPlayOrder(long goodsId, LocalDate playAt);
    PlayTimeDTO findPlayTimeDTO(long playTimeId, long goodsId);
    List<SeatCountDTO> findSeatCount(long playTimeId);

}
