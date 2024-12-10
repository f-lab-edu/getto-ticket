package com.flab.gettoticket.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeatDetail {
    private long bookingSeatId;
    private String bookingSeatStatus;
    private String zoneName;
    private int grade;
    private int price;
    private String seatName;
    private int col;
    private int floor;
}
