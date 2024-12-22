package com.flab.gettoticket.exception.booking;

public class BookingNotFoundException extends BookingException{
    public BookingNotFoundException(String message) {
        super(message);
    }

    public BookingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
