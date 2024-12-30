package com.flab.gettoticket.dto;

import com.flab.gettoticket.enums.BookingStatus;
import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryBookingRequest {
    private long goodsId;
    private long playTimeId;
    private String userId;
    private List<Long> seatIdList;
}
