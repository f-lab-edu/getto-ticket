package com.flab.gettoticket.controller;

import com.flab.gettoticket.dto.PlayTimeDTO;
import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.handler.GlobalExceptionHandler;
import com.flab.gettoticket.entity.PlayTime;
import com.flab.gettoticket.service.PlayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PlayControllerTest {

    @InjectMocks
    private PlayController playController;
    @Mock
    private PlayService playService;

    private MockMvc mockMvc;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(playController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("공연일 리스트 조회")
    void playAtList() throws Exception {
        //given
        long goodsId = 1L;
        LocalDate startDate = LocalDate.parse("20241201", formatter);
        LocalDate endDate = LocalDate.parse("20241203", formatter);

        doReturn(Arrays.asList("2024-12-01", "2024-12-03"))
                .when(playService).findPlayAtList(goodsId, startDate, endDate);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/play-time/list")
                        .param("goodsId", String.valueOf(goodsId))
                        .param("startDate", String.valueOf(startDate))
                        .param("endDate", String.valueOf(endDate))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]").value("2024-12-01"))
                .andExpect(jsonPath("$.data[1]").value("2024-12-03"))
                .andReturn();
    }

    @Test
    @DisplayName("특정 공연일 n회차의 구역별 잔여 좌석 개수")
    void playOrderList() throws Exception {
        //given
        long goodsId = 1L;
        LocalDate playAt = LocalDate.parse("20241130", formatter);

        doReturn(Arrays.asList(
                new PlayTime(playAt, 1, 1600, 1),
                new PlayTime(playAt, 2, 1800, 2)
        )).when(playService).findPlayOrder(goodsId, playAt);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/play-time/order/list")
                        .param("goodsId", String.valueOf(goodsId))
                        .param("playAt", playAt.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].playOrder").value("1"))
                .andExpect(jsonPath("$.data[0].playTime").value("1600"))
                .andExpect(jsonPath("$.data[1].playOrder").value("2"))
                .andExpect(jsonPath("$.data[1].playTime").value("1800"))
                .andReturn();
    }

    @Test
    @DisplayName("특정 공연일 첫번째 회차의 구역별 잔여 좌석 개수")
    void playOrderInfo() throws Exception {
        //given
        long playTimeId = 1L;
        long goodsId = 1L;
        LocalDate playAt = LocalDate.parse("20241130", formatter);
        PlayTime playTime = new PlayTime(playAt, 1, 1600, 1);
        List<String> actorList = new ArrayList<>();

        PlayTimeDTO playTimeDTO = new PlayTimeDTO(playTime
                , Arrays.asList(
                        new SeatCountDTO("1", "VIP", 3),
                        new SeatCountDTO("2", "S", 4)
                )
                , actorList
        );

        doReturn(playTimeDTO)
                .when(playService).findPlayTimeDTO(playTimeId, goodsId);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/play-time/order")
                        .param("playTimeId", String.valueOf(playTimeId))
                        .param("goodsId", String.valueOf(goodsId))
                        .param("playAt", playAt.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.playTime.playOrder").value(1))
                .andExpect(jsonPath("$.data.seatCountList[0].grade").value("1"))
                .andExpect(jsonPath("$.data.seatCountList[0].zoneName").value("VIP"))
                .andExpect(jsonPath("$.data.seatCountList[0].count").value(3))
                .andReturn();
    }
}