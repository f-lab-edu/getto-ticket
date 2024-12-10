package com.flab.gettoticket.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingListResponse {
    private LocalDateTime bookingAt;
    private long bookingId;
    private String bookingStatus;
    private String goodsTitle;
    private LocalDate playAt;
    private int ticketCount;
}
