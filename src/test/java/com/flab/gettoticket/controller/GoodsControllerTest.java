package com.flab.gettoticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flab.gettoticket.entity.Goods;
import com.flab.gettoticket.entity.Zone;
import com.flab.gettoticket.handler.GlobalExceptionHandler;
import com.flab.gettoticket.service.GoodsService;
import com.flab.gettoticket.util.PageRequestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GoodsControllerTest {

    @InjectMocks
    private GoodsController goodsController;

    @Mock
    private GoodsService goodsService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper(); // JSON 직렬화 도구
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(goodsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("상품 조회")
    void findGoodsList() throws Exception {
        //given
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequestUtil.of(page, size);

        doReturn(createGoodsList())
                .when(goodsService).findGoodsList(pageable);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/goods/list")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].genreName").value("서울"))
                .andExpect(jsonPath("$.data[0].title").value("지킬앤하이드"))
                .andExpect(jsonPath("$.data[1].genreName").value("경기"))
                .andExpect(jsonPath("$.data[1].title").value("웃는남자"))
                .andReturn();
    }

    @Test
    @DisplayName("상품 상세 조회")
    void findGoods() throws Exception {
        //given
        long id = 1;
        Goods expectedGoods = new Goods(1, "서울", "지킬앤하이드", "", LocalDate.parse("2024-11-30", formatter), LocalDate.parse("2025-01-30", formatter), "1900", "샤롯데", "", "", 1, 2);

        doReturn(expectedGoods)
                .when(goodsService).findGoods(id);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/goods/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.genreName").value("서울"))
                .andExpect(jsonPath("$.data.title").value("지킬앤하이드"))
                .andReturn();
    }

    @Test
    @DisplayName("상품 등록")
    void addGoods() throws Exception {
        //given
        long id = 1;
        Goods goods = new Goods(1, "서울", "지킬앤하이드", "", LocalDate.parse("2024-11-30"), LocalDate.parse("2025-01-30"), "1900", "샤롯데", "", "", 1, 2);
        String goodsJson = objectMapper.writeValueAsString(goods);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/goods/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson));

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("상품 수정")
    void modifyGoods() throws Exception {
        //given
        long id = 1;
        Goods goods = new Goods(1, "서울", "지킬앤하이드", "", LocalDate.parse("2024-11-30"), LocalDate.parse("2025-01-30"), "1900", "샤롯데", "", "", 1, 2);
        String goodsJson = objectMapper.writeValueAsString(goods);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch("/goods/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson));

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("상품 삭제")
    void removeGoods() throws Exception {
        //given
        long id = 1;
        Goods goods = new Goods(1, "서울", "지킬앤하이드", "", LocalDate.parse("2024-11-30"), LocalDate.parse("2025-01-30"), "1900", "샤롯데", "", "", 1, 2);
        String goodsJson = objectMapper.writeValueAsString(goods);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/goods/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson));

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("좌석 가격 조회")
    void zonePrice() throws Exception {
        //given
        long id = 1;

        doReturn(createZoneList())
                .when(goodsService).findZonePrice(id);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/goods/zone/price/{id}", id)
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

    private List<Goods> createGoodsList() {
        return Arrays.asList(
                new Goods(1, "서울", "지킬앤하이드", "", LocalDate.parse("2024-11-30"), LocalDate.parse("2025-01-30"), "1900", "샤롯데", "", "", 1, 2)
                , new Goods(2, "경기", "웃는남자", "", LocalDate.parse("2024-11-30"), LocalDate.parse("2025-01-30"), "1900", "샤롯데", "", "", 1, 2)
        );
    }

    private List<Zone> createZoneList() {
        return Arrays.asList(
                new Zone(1, "1", "VIP", 170000)
                , new Zone(2, "2", "R", 140000)
        );
    }
}