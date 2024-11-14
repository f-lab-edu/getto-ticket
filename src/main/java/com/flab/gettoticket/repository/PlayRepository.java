package com.flab.gettoticket.repository;

import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.model.PlayTime;
import com.flab.gettoticket.model.Zone;

import java.util.List;

public interface PlayRepository {
    List<String> selectPlayAtList(String goodsId, String startDate, String endDate);
    List<PlayTime> selectTimeTable(String goodsId, String playAt);
    List<SeatCountDTO> selectSeatCount(String timeId);
}
