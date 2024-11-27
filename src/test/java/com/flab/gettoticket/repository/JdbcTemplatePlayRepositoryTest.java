package com.flab.gettoticket.repository;

import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.model.PlayTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        List<LocalDate> list = playRepository.selectPlayAtList(goodsId, startDate, endDate);

        //then
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("대상 공연일의 공연 시간표")    //대상 공연일(yymmdd)
    void selectTimeTable() {
        //given
        long goodsId = 1L;
        LocalDate playAt = LocalDate.parse("20241130", formatter);

        //when
        List<PlayTime> list = playRepository.selectTimeTable(goodsId, playAt);

        //then
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("잔여좌석 개수")
    void selectSeatCount() {
        //given
        long playTimeId = 1;

        //when
        List<SeatCountDTO> list = playRepository.selectSeatCount(playTimeId);

        //then
        assertThat(list.size()).isEqualTo(0);
    }
}