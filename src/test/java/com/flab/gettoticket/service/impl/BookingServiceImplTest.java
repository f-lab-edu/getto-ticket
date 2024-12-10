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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private GoodsRepository goodsRepository;
    @Mock
    private PlayRepository playRepository;
    @Mock
    private SeatRepository seatRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Test
    @DisplayName("내 예매 리스트 조회")
    void findBookingList() {
        // given
        long id = 1L;
        String email = "test2@gmail.com";

        List<Booking> bookingList = Arrays.asList(
                Booking.builder()
                        .id(1L)
                        .status("upcoming")
                        .bookingAt(LocalDateTime.now())
                        .goodsId(101L)
                        .playTimeId(201L)
                        .build()
        );

        Goods goods = Goods.builder()
                .id(101L)
                .title("킹키부츠")
                .build();

        PlayTime playTime = PlayTime.builder()
                .playTimeId(201L)
                .playAt(LocalDate.parse("20241203", formatter))
                .build();

        when(bookingRepository.selectBookingList(email)).thenReturn(bookingList);
        when(goodsRepository.selectGoods(101L)).thenReturn(goods);
        when(playRepository.selectTimeTable(201L, 101L)).thenReturn(playTime);
        when(bookingRepository.selectValidBookingSeatCount(1L)).thenReturn(2);

        // when
        List<BookingListResponse> response = bookingServiceImpl.findBookingList(email);

        // then
        assertThat(response).isNotNull();
        assertThat(response.get(0).getGoodsTitle()).isEqualTo("킹키부츠");
        assertThat(response.get(0).getTicketCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("예매 내역 상세")
    void findBooking() {
        // given
        long id = 1L;
        String email = "test2@gmail.com";

        Booking booking = Booking.builder()
                .id(1L)
                .status("upcoming")
                .bookingAt(LocalDateTime.now())
                .goodsId(101L)
                .playTimeId(201L)
                .userName("test2")
                .build();

        Goods goods = Goods.builder()
                .id(101L)
                .title("웃는 남자")
                .placeName("공연장")
                .build();

        PlayTime playTime = PlayTime.builder()
                .playTimeId(201L)
                .playAt(LocalDate.now())
                .playTime(120)
                .build();

        List<BookingSeatDetail> seatDetails = Arrays.asList(
                BookingSeatDetail.builder()
                        .bookingSeatId(1L)
                        .bookingSeatStatus("upcoming")
                        .build()
                , BookingSeatDetail.builder()
                        .bookingSeatId(2L)
                        .bookingSeatStatus("upcoming")
                        .build()
        );

        when(bookingRepository.selectBooking(id, email)).thenReturn(booking);
        when(bookingRepository.selectBookingSeatDetailList(id)).thenReturn(seatDetails);
        when(goodsRepository.selectGoods(101L)).thenReturn(goods);
        when(playRepository.selectTimeTable(201L, 101L)).thenReturn(playTime);

        // when
        BookingDetailResponse response = bookingServiceImpl.findBooking(id, email);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserName()).isEqualTo("test2");
        assertThat(response.getGoodsTitle()).isEqualTo("웃는 남자");
        assertThat(response.getPlayTime()).isEqualTo(120);
    }

    @Test
    @DisplayName("예매 취소하기")
    void modifyBooking() {
        // given
        CancelBookingRequest cancelBookingRequest = CancelBookingRequest.builder()
                .bookingId(1L)
                .userId("test2@gmail.com")
                .status("cancelled")
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .userId("test2@gmail.com")
                .status("cancelled")
                .build();

        doAnswer(invocation -> {
            Booking updateBooking = invocation.getArgument(0);

            assertThat(updateBooking.getId()).isEqualTo(1L);
            assertThat(updateBooking.getUserId()).isEqualTo("test2@gmail.com");

            return 1;
        }).when(bookingRepository).updateBooking(any(Booking.class));

        long bookingId = 1L;

        List<BookingSeat> bookingSeatsList = Arrays.asList(
                BookingSeat.builder()
                        .id(1L)
                        .seatId(100L)
                        .status("cancelled")
                        .bookingId(bookingId)
                        .build(),
                BookingSeat.builder()
                        .id(2L)
                        .seatId(101L)
                        .status("cancelled")
                        .bookingId(bookingId)
                        .build()
        );

        when(bookingRepository.selectBookingSeat(bookingId)).thenReturn(bookingSeatsList);

        doAnswer(invocation -> {
            BookingSeat bookingSeat = invocation.getArgument(0); // 전달된 BookingSeat 객체 가져오기

            assertThat(bookingSeat.getBookingId()).isEqualTo(1L);
            assertThat(bookingSeat.getStatus()).isEqualTo("cancelled");

            return 1;
        }).when(bookingRepository).updateBookingSeat(any(BookingSeat.class));

        List<Long> seatIdList = Arrays.asList(1L, 2L);
        assertThat(seatIdList.get(0)).isEqualTo(bookingSeatsList.get(0).getId());

        //좌석 상태 변경
        doAnswer(invocation -> {
            long seatId = invocation.getArgument(0);
            String saleYn = invocation.getArgument(1);
            assertThat(saleYn).isEqualTo("N");

            if (seatId == 0L) {
                return 0;
            }

            return 1;
        }).when(seatRepository).updateSeatSaleYn(anyLong(), anyString());

        String saleYn = "N";
        for (long seatId : seatIdList) {
            int result = seatRepository.updateSeatSaleYn(seatId, saleYn);

            if (seatId == 0L) {
                assertEquals(0, result);
            } else {
                assertEquals(1, result);
            }
        }

        // when
        int result = bookingServiceImpl.modifyBookingToCancel(cancelBookingRequest);

        // then
        assertThat(result).isEqualTo(1);

        // verify
        verify(bookingRepository).updateBooking(any(Booking.class));
    }

    @Test
    @DisplayName("예매하기")
    void addBooking() {
        //given
        List<Long> seatIdList = Arrays.asList(1L, 2L);

        AddBookingRequest addBookingRequest = AddBookingRequest.builder()
                .status("upcoming")
                .goodsId(1L)
                .playTimeId(3L)
                .userId("test2@gmail.com")
                .userName("test2")
                .seatIdList(seatIdList)
                .build();

        long bookingSeq = 20L;

        doAnswer(invocation -> {
            return bookingSeq;
        }).when(bookingRepository).insertBooking(any(Booking.class));

        doAnswer(invocation -> {
            BookingSeat bookingSeat = invocation.getArgument(0);

            assertNotNull(bookingSeat);
            assertEquals(bookingSeq, bookingSeat.getBookingId());
            assertEquals(addBookingRequest.getStatus(), bookingSeat.getStatus());

            if (bookingSeat.getSeatId() == 0L) {
                return 0;
            }

            return 1;
        }).when(bookingRepository).insertBookingSeat(any(BookingSeat.class));

        // 예매 좌석 상태 처리
        for (long seatId : seatIdList) {
            BookingSeat bookingSeat = BookingSeat.builder()
                    .status(addBookingRequest.getStatus())
                    .bookingId(bookingSeq)
                    .seatId(seatId)
                    .build();

            int result = bookingRepository.insertBookingSeat(bookingSeat);

            if (seatId == 0L) {
                assertEquals(0, result);
            } else {
                assertEquals(1, result);
            }
        }

        //좌석 상태 변경
        doAnswer(invocation -> {
            return 1;
        }).when(seatRepository).updateSeatSaleYn(anyLong(), anyString());

        //when
        int result = bookingServiceImpl.addBooking(addBookingRequest);

        //then
        assertThat(result).isEqualTo(1);
    }
}