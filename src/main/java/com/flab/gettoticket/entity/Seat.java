package com.flab.gettoticket.entity;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    private long id;
    private String name;
    private int col;
    private int floor;
    private char saleYn;
    private long goodsId;
    private long placeId;
    private long zoneId;
    private long playId;
}
