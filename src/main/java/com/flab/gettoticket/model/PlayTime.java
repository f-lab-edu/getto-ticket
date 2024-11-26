package com.flab.gettoticket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class PlayTime {
    private LocalDate playAt;
    private int playOrder;
    private int playTime;
    private long playTimeId;
}
