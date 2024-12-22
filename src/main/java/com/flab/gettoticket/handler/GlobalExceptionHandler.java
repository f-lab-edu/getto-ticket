package com.flab.gettoticket.handler;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.common.ApiResponseCode;
import com.flab.gettoticket.exception.booking.BookingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ApiResponse<Void>> handleBookingException(BookingException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.create(ApiResponseCode.FAIL.getCode(), ex.getMessage()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
//        log.error("예외 발생: {}", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ApiResponse.create(ApiResponseCode.FAIL.getCode(), ex.getMessage()));
//    }
}