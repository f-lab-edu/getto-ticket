package com.flab.gettoticket.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayOrderListResponse {
    private LocalDate playAt;
    private int playOrder;
    private int playTime;
    private long playTimeId;
}
