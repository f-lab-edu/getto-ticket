package com.flab.gettoticket.repository;

import com.flab.gettoticket.dto.SeatCountResponse;
import com.flab.gettoticket.entity.PlayTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JdbcTemplatePlayRepositoryTest {
    @Mock
    private PlayRepository playRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Test
    @DisplayName("공연일 리스트 조회")
    void selectPlayAtList() {
        //given
        long goodsId = 1L;
        LocalDate startDate = LocalDate.parse("20241130", formatter);
        LocalDate endDate = LocalDate.parse("20241203", formatter);

        //when
        when(playRepository.selectPlayAtList(goodsId, startDate, endDate))
                .thenReturn(Collections.emptyList());

        List<String> list = playRepository.selectPlayAtList(goodsId, startDate, endDate);

        //then
        assertThat(list).isEmpty();
    }

    @Test
    @DisplayName("대상 공연일의 공연 시간표")    //대상 공연일(yymmdd)
    void selectTimeTable() {
        //given
        long goodsId = 1L;
        LocalDate playAt = LocalDate.parse("20241130", formatter);

        //when
        when(playRepository.selectTimeTableList(goodsId, playAt))
                .thenReturn(Collections.emptyList());

        List<PlayTime> list = playRepository.selectTimeTableList(goodsId, playAt);

        //then
        assertThat(list).isEmpty();
    }

    @Test
    @DisplayName("잔여좌석 개수")
    void selectSeatCount() {
        //given
        long playTimeId = 1;

        //when
        when(playRepository.selectSeatCount(playTimeId))
                .thenReturn(Collections.emptyList());

        List<SeatCountResponse> list = playRepository.selectSeatCount(playTimeId);

        //then
        assertThat(list).isEmpty();
    }
}