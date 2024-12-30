package com.flab.gettoticket.service;

import com.flab.gettoticket.dto.AddBookingRequest;
import com.flab.gettoticket.dto.BookingDetailResponse;
import com.flab.gettoticket.dto.BookingListResponse;
import com.flab.gettoticket.dto.CancelBookingRequest;
import com.flab.gettoticket.entity.BookingSeat;

import java.util.List;

public interface BookingService {
    List<BookingListResponse> findBookingList(String email);
    BookingDetailResponse findBooking(long id, String email);
    void modifyBookingToCancel(CancelBookingRequest cancelBookingRequest);
    void modifySeatStatus(List<Long> seatIdList, String saleYn);
    void addBooking(AddBookingRequest addBookingRequest);
}
