package com.flab.gettoticket.dto;

import com.flab.gettoticket.entity.PlayTime;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PlayTimeDTO {
    private PlayTime playTime;
    private List<SeatCountDTO> seatCountList;
    private List<String> actorList;

    public PlayTimeDTO(PlayTime playTime, List<SeatCountDTO> seatCountList, List<String> actorList) {
        this.playTime = playTime;
        this.seatCountList = seatCountList;
        this.actorList = actorList;
    }
}
