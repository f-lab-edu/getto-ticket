package com.flab.gettoticket.exception.booking;

public class BookingNullPointException extends BookingException{
    public BookingNullPointException(String message) {
        super(message);
    }

    public BookingNullPointException(String message, Throwable cause) {
        super(message, cause);
    }
}
