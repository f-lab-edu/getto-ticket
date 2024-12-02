package com.flab.gettoticket.dto;

import com.flab.gettoticket.entity.PlayTime;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class SeatDTO {
    private List<PlayTime> timeTableList;
    private List<SeatCountDTO> seatCountList;

    public SeatDTO(List<PlayTime> timeTableList, List<SeatCountDTO> seatCountList) {
        this.timeTableList = timeTableList;
        this.seatCountList = seatCountList;
    }
}
