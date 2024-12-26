package com.flab.gettoticket.entity;

import com.flab.gettoticket.enums.BookingStatus;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeat {
    private long id;
    private BookingStatus status;
    private long bookingId;
    private long seatId;
}
