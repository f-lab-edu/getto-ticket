package com.flab.gettoticket.dto;

import com.flab.gettoticket.enums.BookingStatus;
import lombok.*;

import java.time.LocalDate;
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelBookingRequest {
    private long bookingId;
    private String userId;
    private BookingStatus bookingStatus;
}
