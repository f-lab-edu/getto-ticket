package com.flab.gettoticket.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private long id;
    private String status;
    private long goodsId;
    private long playTimeId;
    private String userId;
    private String userName;
    private LocalDateTime bookingAt;
}