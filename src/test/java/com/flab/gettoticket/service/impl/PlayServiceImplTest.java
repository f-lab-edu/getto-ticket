package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.dto.SeatDTO;
import com.flab.gettoticket.entity.PlayTime;
import com.flab.gettoticket.repository.PlayRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayServiceImplTest {
    @InjectMocks
    private PlayServiceImpl playServiceImpl;

    @Mock
    private PlayRepository playRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Test
    @DisplayName("공연일 리스트 조회")
    void findPlayAtList() {
        //given
        long goodsId = 1L;
        LocalDate startDate = LocalDate.parse("20241130", formatter);
        LocalDate endDate = LocalDate.parse("20241203", formatter);

        when(playRepository.selectPlayAtList(goodsId, startDate, endDate))
                .thenReturn(Arrays.asList("2024-11-30", "2024-12-01", "2024-12-03"));

        //when
        List<String> list = playServiceImpl.findPlayAtList(goodsId, startDate, endDate);

        //then
        assertThat(list).containsExactly("2024-11-30", "2024-12-01", "2024-12-03");

    }

    @Test
    @DisplayName("yymmdd 공연일의 모든 공연 회차 오름차순 조회")
    void findPlayOrder() {
        //given
        long goodsId = 1L;
        LocalDate playAt = LocalDate.parse("20241130", formatter);

        when(playRepository.selectTimeTable(goodsId, playAt))
                .thenReturn(Arrays.asList(
                        new PlayTime(playAt, 1, 1600, 1),
                        new PlayTime(playAt, 1, 1600, 1)
                ));

        //when
        List<PlayTime> list = playServiceImpl.findPlayOrder(goodsId, playAt);

        //then
        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("선택한 시간표의 잔여좌석 개수")
    void findSeatCount() {
        //given
        long playTimeId = 1L;

        when(playRepository.selectSeatCount(playTimeId))
                .thenReturn(Arrays.asList(
                        new SeatCountDTO("1", "VIP", 3),
                        new SeatCountDTO("2", "S", 4)
                ));

        //when
        List<SeatCountDTO> list = playServiceImpl.findSeatCount(playTimeId);

        //then
        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("yymmdd 공연일의 첫번째 회차 잔여좌석 개수")
    void findSeatDTO() {
        long goodsId = 1L;
        LocalDate playAt = LocalDate.parse("20241130", formatter);

        when(playRepository.selectTimeTable(goodsId, playAt))
                .thenReturn(Arrays.asList(
                        new PlayTime(playAt, 1, 1600, 1),
                        new PlayTime(playAt, 1, 1600, 1)
                ));
        when(playRepository.selectSeatCount(1L))
                .thenReturn(Arrays.asList(
                        new SeatCountDTO("1", "VIP", 3),
                        new SeatCountDTO("2", "S", 4)
                ));

        //when
        SeatDTO dto = playServiceImpl.findSeatDTO(goodsId, playAt);

        //then
        assertThat(dto.getTimeTableList()).hasSize(2);
        assertThat(dto.getSeatCountList()).hasSize(2);
    }
}