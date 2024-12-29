package com.flab.gettoticket.dto;

import com.flab.gettoticket.enums.BookingStatus;
import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeatDetail {
    private long bookingSeatId;
    private BookingStatus bookingSeatStatus;
    private String zoneName;
    private int grade;
    private int price;
    private String seatName;
    private int col;
    private int floor;
}
