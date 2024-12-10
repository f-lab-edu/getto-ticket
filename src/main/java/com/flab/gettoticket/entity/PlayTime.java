package com.flab.gettoticket.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayTime {
    private LocalDate playAt;
    private int playOrder;
    private int playTime;
    private long playTimeId;
}
