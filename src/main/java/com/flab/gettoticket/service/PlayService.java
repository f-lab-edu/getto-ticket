package com.flab.gettoticket.service;

import com.flab.gettoticket.dto.PlayOrderListResponse;
import com.flab.gettoticket.dto.PlayTimeResponse;
import com.flab.gettoticket.dto.SeatCountResponse;

import java.time.LocalDate;
import java.util.List;

public interface PlayService {
    List<String> findPlayAtList(long goodsId, LocalDate startDate, LocalDate endDate);
    List<PlayOrderListResponse> findPlayOrder(long goodsId, LocalDate playAt);
    PlayTimeResponse findPlayTimeDTO(long playTimeId, long goodsId);
    List<SeatCountResponse> findSeatCount(long playTimeId);

}
