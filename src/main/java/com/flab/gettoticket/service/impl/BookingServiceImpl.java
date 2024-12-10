package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.dto.*;
import com.flab.gettoticket.entity.Booking;
import com.flab.gettoticket.entity.BookingSeat;
import com.flab.gettoticket.entity.Goods;
import com.flab.gettoticket.entity.PlayTime;
import com.flab.gettoticket.repository.BookingRepository;
import com.flab.gettoticket.repository.GoodsRepository;
import com.flab.gettoticket.repository.PlayRepository;
import com.flab.gettoticket.repository.SeatRepository;
import com.flab.gettoticket.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final GoodsRepository goodsRepository;
    private final PlayRepository playRepository;
    private final SeatRepository seatRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, GoodsRepository goodsRepository, PlayRepository playRepository, SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.goodsRepository = goodsRepository;
        this.playRepository = playRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public List<BookingListResponse> findBookingList(String email) {
        List<Booking> bookingList = bookingRepository.selectBookingList(email);

        List<BookingListResponse> list = new ArrayList<>();

        if(Objects.isNull(bookingList)) {
            log.error("예매 정보 조회 중 예외 발생 email: {}", email);
            throw new RuntimeException("예매 정보 조회에 실패했습니다.");
        }

        for (Booking booking : bookingList) {
            long bookingId = booking.getId();
            String bookingStatus = booking.getStatus();
            LocalDateTime bookingAt = booking.getBookingAt();
            long goodsId = booking.getGoodsId();
            long playTimeId = booking.getPlayTimeId();

            Goods goods = goodsRepository.selectGoods(goodsId);
            PlayTime playTime = playRepository.selectTimeTable(playTimeId, goodsId);

            String goodsTitle = goods.getTitle();
            LocalDate playAt = playTime.getPlayAt();
            int ticketCount = bookingRepository.selectValidBookingSeatCount(bookingId);

            list.add(BookingListResponse.builder()
                    .bookingAt(bookingAt)
                    .bookingId(bookingId)
                    .bookingStatus(bookingStatus)
                    .goodsTitle(goodsTitle)
                    .playAt(playAt)
                    .ticketCount(ticketCount)
                    .build());
        }

        return list;
    }

    @Override
    public BookingDetailResponse findBooking(long id, String email) {
        Booking booking = bookingRepository.selectBooking(id, email);
        List<BookingSeatDetail> bookingSeatDetail = bookingRepository.selectBookingSeatDetailList(id);

        if(Objects.isNull(booking) | Objects.isNull(bookingSeatDetail)) {
            log.error("예매 정보 조회 중 예외 발생 id: {}, email: {}", id, email);
            throw new RuntimeException("예매 정보 조회에 실패했습니다.");
        }

        String userName = booking.getUserName();
        long bookingId = booking.getId();
        String bookingStatus = booking.getStatus();
        LocalDateTime bookingAt = booking.getBookingAt();
        long goodsId = booking.getGoodsId();
        long playTimeId = booking.getPlayTimeId();

        Goods goods = goodsRepository.selectGoods(goodsId);
        PlayTime playTime = playRepository.selectTimeTable(playTimeId, goodsId);

        String goodsTitle = goods.getTitle();
        String placeName = goods.getPlaceName();
        LocalDate playAt = playTime.getPlayAt();
        int time = playTime.getPlayTime();

        return BookingDetailResponse.builder()
                .userName(userName)
                .bookingId(bookingId)
                .bookingStatus(bookingStatus)
                .bookingAt(bookingAt)
                .goodsTitle(goodsTitle)
                .playAt(playAt)
                .playTime(time)
                .placeName(placeName)
                .bookingSeatDetail(bookingSeatDetail)
                .build();
    }

    @Override
    public int modifyBookingToCancel(CancelBookingRequest cancelBookingRequest) {
        long bookingId = cancelBookingRequest.getBookingId();
        String userId = cancelBookingRequest.getUserId();
        String status = cancelBookingRequest.getStatus();

        Booking booking = Booking.builder()
                            .id(bookingId)
                            .userId(userId)
                            .status(status)
                            .build();

        int result = bookingRepository.updateBooking(booking);

        if(result == 0) {
            log.error("예매 취소 중 예외 발생 booking: {}", booking);
            throw new RuntimeException("예매 취소에 실패했습니다.");
        }

        List<BookingSeat> bookingSeatsList = bookingRepository.selectBookingSeat(bookingId);

        if(Objects.isNull(bookingSeatsList)) {
            log.error("취소할 예매 좌석 조회 중 예외 발생 bookingSeatsList: {}", bookingSeatsList);
            throw new RuntimeException("예매 취소에 실패했습니다.");
        }

        for(BookingSeat obj : bookingSeatsList) {
            BookingSeat bookingSeat = BookingSeat.builder()
                    .id(obj.getId())
                    .status(status)
                    .bookingId(bookingId)
                    .build();

            result = bookingRepository.updateBookingSeat(bookingSeat);

            if(result == 0) {
                log.error("예매 취소 중 예외 발생 bookingSeat: {}", bookingSeat);
                throw new RuntimeException("예매 취소에 실패했습니다.");
            }
        }

        String saleYn = "N";
        List<Long> seatIdList = new ArrayList<>();

        for(BookingSeat obj : bookingSeatsList) {
            seatIdList.add(obj.getSeatId());
        }

        result = modifySeatStatus(seatIdList, saleYn);

        return result;
    }

    @Override
    public int modifySeatStatus(List<Long> seatIdList, String saleYn) {
        int result = 0;

        for (Long seatId : seatIdList) {
            log.error("seatId: {}, saleYn: {}", seatId, saleYn);
            result = seatRepository.updateSeatSaleYn(seatId, saleYn);

            if (result == 0) {
                log.error("예매 좌석 상태 변경 중 예외 발생 seatId: {}, saleYn: {}", seatId, saleYn);
                throw new RuntimeException("예매 취소에 실패했습니다.");
            }
        }

        return result;
    }

    @Override
    public int addBooking(AddBookingRequest addBookingRequest) {
        int result = 0;

        String status = addBookingRequest.getStatus();
        long goodsId = addBookingRequest.getGoodsId();
        long playTimeId = addBookingRequest.getPlayTimeId();
        String userId = addBookingRequest.getUserId();
        String userName = addBookingRequest.getUserName();
        List<Long> seatIdList = addBookingRequest.getSeatIdList();

        Booking booking = Booking.builder()
                            .status(status)
                            .goodsId(goodsId)
                            .playTimeId(playTimeId)
                            .userId(userId)
                            .userName(userName)
                            .build();

        //예매 정보
        long bookingId = bookingRepository.insertBooking(booking);

        if(bookingId == 0) {
            log.error("예매 중 예외 발생 bookingId: {}, booking: {}", bookingId, booking);
            throw new RuntimeException("예매에 실패했습니다.");
        }

        //예매 좌석
        for (long seatId : seatIdList) {
            BookingSeat bookingSeat = BookingSeat.builder()
                    .status(status)
                    .bookingId(bookingId)
                    .seatId(seatId)
                    .build();

            result = bookingRepository.insertBookingSeat(bookingSeat);

            if (result == 0) {
                log.error("예매 중 예외 발생 bookingSeat: {}", bookingSeat);
                throw new RuntimeException("예매에 실패했습니다.");
            }
        }

        //좌석 상태 변경
        String saleYn = "Y";
        result = modifySeatStatus(seatIdList, saleYn);

        return result;
    }
}
