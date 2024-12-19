package com.flab.gettoticket.dto;

import com.flab.gettoticket.enums.BookingStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBookingRequest {
    private BookingStatus bookingStatus;
    private long goodsId;
    private long playTimeId;
    private String userId;
    private String userName;
    private List<Long> seatIdList;
}
