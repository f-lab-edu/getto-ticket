package com.flab.gettoticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flab.gettoticket.entity.Zone;
import com.flab.gettoticket.handler.GlobalExceptionHandler;
import com.flab.gettoticket.service.GoodsService;
import com.flab.gettoticket.service.SeatService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SeatControllerTest {

    @InjectMocks
    private SeatController seatController;

    @Mock
    private SeatService seatService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(seatController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("좌석 가격 조회")
    void zonePrice() throws Exception {
        //given
        long id = 1;

        doReturn(createZoneList())
                .when(seatService).findZonePrice(id);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/seat/price/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].grade").value("1"))
                .andExpect(jsonPath("$.data[0].name").value("VIP"))
                .andExpect(jsonPath("$.data[1].grade").value("2"))
                .andExpect(jsonPath("$.data[1].name").value("R"))
                .andReturn();
    }

    private List<Zone> createZoneList() {
        return Arrays.asList(
                new Zone(1, "1", "VIP", 170000)
                , new Zone(2, "2", "R", 140000)
        );
    }
}