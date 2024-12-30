package com.flab.gettoticket;

import com.flab.gettoticket.dto.TemporaryBookingRequest;
import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.enums.SeatStatus;
import com.flab.gettoticket.repository.RedisDistributedRepository;
import com.flab.gettoticket.repository.RedisSeatRepository;
import com.flab.gettoticket.repository.SeatRepository;
import com.flab.gettoticket.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TemporaryBookingTest {

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    @Mock
    private RedisSeatRepository redisSeatRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private RedisDistributedRepository redisDistributedRepository;

    private static final long GOODS_ID = 1L;
    private static final long PLAY_ID = 5L;
    private static final long SEAT_ID = 1L;

    @BeforeEach
    void setup() {
        // Mock 초기화
        Seat seat = Seat.builder()
                .id(SEAT_ID)
                .name("A1")
                .col(1)
                .floor(1)
                .statusCode(SeatStatus.FOR_SALE.getCode())
                .goodsId(GOODS_ID)
                .placeId(1L)
                .zoneId(1L)
                .playId(PLAY_ID)
                .build();

        String redisKey = GOODS_ID + ":" + PLAY_ID;

        when(redisSeatRepository.selectSeat(redisKey, SEAT_ID)).thenReturn(null);
        when(seatRepository.selectSeat(SEAT_ID)).thenReturn(seat);
        when(redisDistributedRepository.tryLock(anyString())).thenReturn(true);
    }

    @Test
    void testConcurrentTemporaryBooking() throws InterruptedException {
        int threadCount = 10; // 동시 요청 개수
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    TemporaryBookingRequest request = TemporaryBookingRequest.builder()
                            .goodsId(GOODS_ID)
                            .playTimeId(PLAY_ID)
                            .seatIdList(List.of(SEAT_ID))
                            .build();
                    bookingServiceImpl.temporaryBooking(request);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 쓰레드가 작업을 끝낼 때까지 대기
        executorService.shutdown();

        // 예약 상태 검증
        String redisKey = GOODS_ID + ":" + PLAY_ID;
        ArgumentCaptor<Seat> seatCaptor = ArgumentCaptor.forClass(Seat.class);

        verify(redisSeatRepository, atLeastOnce()).insertSeatInfo(eq(redisKey), seatCaptor.capture());
        Seat capturedSeat = seatCaptor.getValue();
        assertNotNull(capturedSeat);
        assertEquals(SeatStatus.RESERVED.getCode(), capturedSeat.getStatusCode());

        verify(redisDistributedRepository, times(threadCount)).tryLock(anyString());
    }
}

