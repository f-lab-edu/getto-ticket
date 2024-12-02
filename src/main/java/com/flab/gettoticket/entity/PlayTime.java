package com.flab.gettoticket.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class PlayTime {
    private LocalDate playAt;
    private int playOrder;
    private int playTime;
    private long playTimeId;

    public PlayTime(LocalDate playAt, int playOrder, int playTime, long playTimeId) {
        this.playAt = playAt;
        this.playOrder = playOrder;
        this.playTime = playTime;
        this.playTimeId = playTimeId;
    }
}
