package com.flab.gettoticket.dto;

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
    private String status;
}
