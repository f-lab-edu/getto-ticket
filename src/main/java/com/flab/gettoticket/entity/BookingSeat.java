package com.flab.gettoticket.entity;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeat {
    private long id;
    private String status;
    private long bookingId;
    private long seatId;
}
