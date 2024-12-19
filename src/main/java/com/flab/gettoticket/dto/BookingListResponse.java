package com.flab.gettoticket.dto;

import com.flab.gettoticket.enums.BookingStatus;
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
    private BookingStatus bookingStatus;
    private String goodsTitle;
    private LocalDate playAt;
    private int ticketCount;
}
