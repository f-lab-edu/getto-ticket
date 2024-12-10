package com.flab.gettoticket.repository;

import com.flab.gettoticket.dto.BookingSeatDetail;
import com.flab.gettoticket.entity.Booking;
import com.flab.gettoticket.entity.BookingSeat;

import java.util.List;

public interface BookingRepository {
    List<Booking> selectBookingList(String email);
    Booking selectBooking(long id, String email);
    List<BookingSeat> selectBookingSeat(long bookingId);
    List<BookingSeatDetail> selectBookingSeatDetailList(long bookingId);
    int selectValidBookingSeatCount(long bookingId);
    long selectBookingLastSeq();
    long selectBookingSeatLastSeq();
    int updateBooking(Booking booking);
    int updateBookingSeat(BookingSeat bookingSeat);
    long insertBooking(Booking booking);
    int insertBookingSeat(BookingSeat bookingSeat);
}
