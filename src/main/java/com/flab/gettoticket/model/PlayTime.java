package com.flab.gettoticket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlayTime {
    private String playAt;
    private int playOrder;
    private int playTime;
    private String playTimeId;
}
