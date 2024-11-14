package com.flab.gettoticket.service;

import com.flab.gettoticket.dto.SeatDTO;
import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.model.PlayTime;
import com.flab.gettoticket.model.Zone;

import java.util.List;

public interface PlayService {
    SeatDTO findSeatDTO(String goodsId, String playAt);
    List<String> findPlayAtList(String goodsId, String startDate, String endDate);
    List<SeatCountDTO> findSeatCount(String playTimeId);
    List<PlayTime> findPlayOrder(String goodsId, String playAt);
}
