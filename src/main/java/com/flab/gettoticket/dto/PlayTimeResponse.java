package com.flab.gettoticket.dto;

import com.flab.gettoticket.entity.PlayTime;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PlayTimeResponse {
    private PlayTime playTime;
    private List<SeatCountResponse> seatCountList;
    private List<String> actorList;

    public PlayTimeResponse(PlayTime playTime, List<SeatCountResponse> seatCountList, List<String> actorList) {
        this.playTime = playTime;
        this.seatCountList = seatCountList;
        this.actorList = actorList;
    }
}
