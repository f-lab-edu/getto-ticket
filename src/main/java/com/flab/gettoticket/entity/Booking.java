package com.flab.gettoticket.entity;

import com.flab.gettoticket.enums.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private long id;
    private BookingStatus status;
    private long goodsId;
    private long playTimeId;
    private String userId;
    private String userName;
    private LocalDateTime bookingAt;
}