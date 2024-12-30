package com.flab.gettoticket.service;

import com.flab.gettoticket.dto.*;

import java.util.List;

public interface BookingService {
    List<BookingListResponse> findBookingList(String email);
    BookingDetailResponse findBooking(long id, String email);
    void modifyBookingToCancel(CancelBookingRequest cancelBookingRequest);
    void modifySeatStatus(List<Long> seatIdList, int seatStatusCode);
    void addBooking(AddBookingRequest addBookingRequest);
}
