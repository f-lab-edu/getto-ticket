package com.flab.gettoticket.validation;

import com.flab.gettoticket.entity.Booking;
import com.flab.gettoticket.exception.booking.BookingIllegalArgumentException;
import com.flab.gettoticket.exception.booking.BookingNullPointException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookingValidator {

    public static void insert(Booking booking) {
        if (booking == null) {
            log.error("Booking 객체 null: {}", booking);
            throw new BookingNullPointException("유효하지 않은 예매 정보입니다.");
        }

        if (booking.getGoodsId() <= 0) {
            throw new BookingIllegalArgumentException("유효한 공연번호가 필요합니다.");
        }

        if (booking.getPlayTimeId() <= 0) {
            throw new BookingIllegalArgumentException("유효한 공연일정 번호가 필요합니다.");
        }

        if (booking.getUserId() == null || booking.getUserId().isEmpty()) {
            throw new BookingIllegalArgumentException("사용자 이메일은 필수값입니다.");
        }

        if (booking.getUserName() == null || booking.getUserName().isEmpty()) {
            throw new BookingIllegalArgumentException("사용자명은 필수값입니다.");
        }

        if (booking.getStatus() == null) {
            throw new BookingIllegalArgumentException("예매 상태는 필수값입니다.");
        }
    }

    public static void update(Booking booking) {
        if (booking == null) {
            log.error("Booking 객체 null: {}", booking);
            throw new BookingNullPointException("유효하지 않은 예매 정보입니다.");
        }

        if (booking.getId() <= 0) {
            throw new BookingIllegalArgumentException("유효한 예매번호가 필요합니다.");
        }

        if (booking.getUserId() == null || booking.getUserId().isEmpty()) {
            throw new BookingIllegalArgumentException("사용자 이메일은 필수값입니다.");
        }

        if (booking.getStatus() == null) {
            throw new BookingIllegalArgumentException("예매 상태는 필수값입니다.");
        }
    }
}
