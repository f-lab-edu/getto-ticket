package com.flab.gettoticket.exception.booking;

public class BookingIllegalArgumentException extends BookingException{
    public BookingIllegalArgumentException(String message) {
        super(message);
    }

    public BookingIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
