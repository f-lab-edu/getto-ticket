package com.flab.gettoticket.dto;

import com.flab.gettoticket.enums.BookingStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailResponse {
    private String userName;
    private long bookingId;
    private BookingStatus bookingStatus;
    private LocalDateTime bookingAt;
    private String goodsTitle;
    private LocalDate playAt;
    private int playTime;
    private String placeName;
    private List<BookingSeatDetail> bookingSeatDetail;
}
