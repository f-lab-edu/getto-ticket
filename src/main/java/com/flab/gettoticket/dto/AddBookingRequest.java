package com.flab.gettoticket.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBookingRequest {
    private String status;
    private long goodsId;
    private long playTimeId;
    private String userId;
    private String userName;
    private List<Long> seatIdList;
}
