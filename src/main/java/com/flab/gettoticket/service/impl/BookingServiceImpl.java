package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.dto.*;
import com.flab.gettoticket.entity.*;
import com.flab.gettoticket.enums.BookingStatus;
import com.flab.gettoticket.enums.RedisKey;
import com.flab.gettoticket.enums.SeatStatus;
import com.flab.gettoticket.exception.booking.BookingIllegalArgumentException;
import com.flab.gettoticket.exception.booking.BookingNotFoundException;
import com.flab.gettoticket.exception.lock.DistributedIllegalMonitorStateException;
import com.flab.gettoticket.exception.lock.DistributedInterruptedException;
import com.flab.gettoticket.repository.*;
import com.flab.gettoticket.service.BookingService;
import com.flab.gettoticket.validation.BookingValidator;
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
    private final RedisSeatRepository redisSeatRepository;
    private final RedisDistributedRepository redisDistributedRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, GoodsRepository goodsRepository, PlayRepository playRepository, SeatRepository seatRepository, RedisSeatRepository redisSeatRepository, RedisDistributedRepository redisDistributedRepository) {
        this.bookingRepository = bookingRepository;
        this.goodsRepository = goodsRepository;
        this.playRepository = playRepository;
        this.seatRepository = seatRepository;
        this.redisSeatRepository = redisSeatRepository;
        this.redisDistributedRepository = redisDistributedRepository;
    }

    @Override
    public List<BookingListResponse> findBookingList(String email) {
        List<Booking> bookingList = bookingRepository.selectBookingList(email);

        List<BookingListResponse> list = new ArrayList<>();

        if(bookingList.isEmpty()) {
            log.error("예매 정보 조회 중 예외 발생 email: {}", email);
            throw new BookingNotFoundException("조회된 예매 정보가 없습니다.");
        }

        for (Booking booking : bookingList) {
            long bookingId = booking.getId();
            BookingStatus status = booking.getStatus();
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
                    .bookingStatus(status)
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

        if(Objects.isNull(booking) || bookingSeatDetail.isEmpty()) {
            log.error("예매 정보 조회 중 예외 발생 id: {}, email: {}", id, email);
            throw new BookingNotFoundException("조회된 예매 정보가 없습니다.");
        }

        String userName = booking.getUserName();
        long bookingId = booking.getId();
        BookingStatus status = booking.getStatus();
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
                .bookingStatus(status)
                .bookingAt(bookingAt)
                .goodsTitle(goodsTitle)
                .playAt(playAt)
                .playTime(time)
                .placeName(placeName)
                .bookingSeatDetail(bookingSeatDetail)
                .build();
    }

    @Override
    public void modifyBookingToCancel(CancelBookingRequest cancelBookingRequest) {
        long bookingId = cancelBookingRequest.getBookingId();
        String userId = cancelBookingRequest.getUserId();
        BookingStatus bookingStatus = cancelBookingRequest.getBookingStatus();

        Booking booking = Booking.builder()
                            .id(bookingId)
                            .userId(userId)
                            .status(bookingStatus)
                            .build();

        BookingValidator.update(booking);

        bookingRepository.updateBooking(booking);

        List<BookingSeat> bookingSeatsList = bookingRepository.selectBookingSeat(bookingId);

        if(bookingSeatsList.isEmpty()) {
            log.error("취소할 예매 좌석 조회 중 예외 발생 bookingSeatsList: {}", bookingSeatsList);
            throw new BookingNotFoundException("조회된 좌석이 없습니다.");
        }

        for(BookingSeat obj : bookingSeatsList) {
            BookingSeat bookingSeat = BookingSeat.builder()
                    .id(obj.getId())
                    .status(bookingStatus)
                    .bookingId(bookingId)
                    .userId(userId)
                    .build();

            bookingRepository.updateBookingSeat(bookingSeat);
        }

        int statusCode = 300;
        List<Long> seatIdList = new ArrayList<>();

        for(BookingSeat obj : bookingSeatsList) {
            seatIdList.add(obj.getSeatId());
        }

        modifySeatStatus(seatIdList, statusCode);
    }

    @Override
    public void modifySeatStatus(List<Long> seatIdList, int statusCode) {
        for (Long seatId : seatIdList) {
            seatRepository.updateSeatStatusCode(seatId, statusCode);
        }
    }

    @Override
    public void addBooking(AddBookingRequest addBookingRequest) {
        BookingStatus bookingStatus = addBookingRequest.getBookingStatus();
        long goodsId = addBookingRequest.getGoodsId();
        long playTimeId = addBookingRequest.getPlayTimeId();
        String userId = addBookingRequest.getUserId();
        String userName = addBookingRequest.getUserName();
        List<Long> seatIdList = addBookingRequest.getSeatIdList();

        Booking booking = Booking.builder()
                            .status(bookingStatus)
                            .goodsId(goodsId)
                            .playTimeId(playTimeId)
                            .userId(userId)
                            .userName(userName)
                            .build();

        BookingValidator.insert(booking);

        //예매 정보
        long bookingId = bookingRepository.insertBooking(booking);

        if(bookingId <= 0) {
            log.error("예매 중 예외 발생 bookingId: {}, booking: {}", bookingId, booking);
            throw new BookingIllegalArgumentException("예매번호가 필요합니다.");
        }

        if(seatIdList.isEmpty()) {
            log.error("예매 중 예외 발생 seatIdList: {}", seatIdList);
            throw new BookingIllegalArgumentException("좌석번호는 필수값입니다.");
        }

        //예매 좌석
        for (long seatId : seatIdList) {
            BookingSeat bookingSeat = BookingSeat.builder()
                    .status(bookingStatus)
                    .bookingId(bookingId)
                    .seatId(seatId)
                    .userId(userId)
                    .build();

            //예매 좌석 확정
            bookingRepository.insertBookingSeat(bookingSeat);
        }

        //좌석 상태 변경
        modifySeatStatus(seatIdList, SeatStatus.SOLD_OUT.getCode());
    }

    /**
     * 1. 사용자가 선택한 좌석 정보 조회
     * 2. 좌석 획득 가능 여부 체크
     * 3. 가능시 락 획득 및 임시 예약
     */
    @Override
    public boolean temporaryBooking(TemporaryBookingRequest temporaryBookingRequest) {
        String userId = temporaryBookingRequest.getUserId();
        long goodsId = temporaryBookingRequest.getGoodsId();
        long playId = temporaryBookingRequest.getPlayTimeId();
        List<Long> seatIdList = temporaryBookingRequest.getSeatIdList();

        for(long seatId : seatIdList) {
            String plainTextKey = String.valueOf(goodsId + ":" + playId);
            String resourceKey = RedisKey.SEAT_KEY.getKey() + plainTextKey;
            boolean isCacheHit = true;

            //좌석 정보 조회
            Seat seat = redisSeatRepository.selectSeat(plainTextKey, seatId);

            if(seat == null) {
                isCacheHit = false;
                seat = seatRepository.selectSeat(seatId);
            }

            log.info("좌석 캐시 존재 여부 isCacheHit: {}", isCacheHit);
            log.info("Seat seat: {}", seat);

            int statusCode = seat.getStatusCode();

            if(statusCode != 100) { //예약 가능
                return false;
            }

            //좌석 획득 가능 여부 체크
            boolean available = false;

            try {
                available = redisDistributedRepository.tryLock(resourceKey);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new DistributedInterruptedException("lock 획득에 실패했습니다.", e);
            } catch (IllegalMonitorStateException e) {
                throw new DistributedIllegalMonitorStateException("이미 unlock 처리된 lock 입니다", e);
            }

            //가능시 좌석 임시 예약
            if(available) {
                log.info("현재 사용자가 lock 획득 userId: {}", userId);

                //redis 좌석 정보 insert/update
                Seat updateSeat = Seat.builder()
                        .id(seatId)
                        .name(seat.getName())
                        .col(seat.getCol())
                        .floor(seat.getFloor())
                        .statusCode(SeatStatus.RESERVED.getCode())
                        .goodsId(seat.getGoodsId())
                        .placeId(seat.getPlaceId())
                        .zoneId(seat.getZoneId())
                        .playId(seat.getPlayId())
                        .build();

                if(isCacheHit) {
                    redisSeatRepository.updateSeatInfo(plainTextKey, updateSeat);
                }
                else {
                    redisSeatRepository.insertSeatInfo(plainTextKey, updateSeat);
                }
            }
        }

        return true;
    }
}
