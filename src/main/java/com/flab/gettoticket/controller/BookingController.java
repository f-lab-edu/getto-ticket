package com.flab.gettoticket.controller;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.common.ApiResponseCode;
import com.flab.gettoticket.dto.AddBookingRequest;
import com.flab.gettoticket.dto.BookingDetailResponse;
import com.flab.gettoticket.dto.BookingListResponse;
import com.flab.gettoticket.dto.CancelBookingRequest;
import com.flab.gettoticket.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // 내 예매
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<BookingListResponse>>> findBookingList(
            @RequestParam String email
    ) {
        List<BookingListResponse> data = bookingService.findBookingList(email);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    // 예매 내역 상세
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDetailResponse>> findBooking(
            @PathVariable long id
            , @RequestParam String email
    ) {
        BookingDetailResponse data = bookingService.findBooking(id, email);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    // 예매 취소하기
    @PostMapping("/cancle")
    public ResponseEntity<ApiResponse<Void>> modifyBooking(@RequestBody CancelBookingRequest cancelBookingRequest) {
        bookingService.modifyBookingToCancel(cancelBookingRequest);

        String message = ApiResponseCode.BOOKING_CANCLE_SUCCESS.getMessage();
        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.BOOKING_CANCLE_SUCCESS.getCode(), message));
    }

    // 예매 하기
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addBooking(@RequestBody AddBookingRequest addBookingRequest) {
        bookingService.addBooking(addBookingRequest);

        String message = ApiResponseCode.BOOKING_SUCCESS.getMessage();
        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.BOOKING_SUCCESS.getCode(), message));
    }
}
