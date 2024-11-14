package com.flab.gettoticket.dto;

import com.flab.gettoticket.model.PlayTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SeatDTO {
    private List<PlayTime> timeTableList;
    private List<SeatCountDTO> seatCountList;
}
