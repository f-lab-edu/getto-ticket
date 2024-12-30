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
                MockMvcRequestBuilders.get("/v1/goods/list")
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
        Goods goods = Goods.builder()
                .id(id)
                .genreName("서울")
                .title("지킬앤하이드")
                .desc("")
                .performanceStartDate(LocalDate.parse("2024-11-30", formatter))
                .performanceEndDate(LocalDate.parse("2025-01-30", formatter))
                .performanceTime("1900")
                .location("샤롯데")
                .x("")
                .y("")
                .genreId(1)
                .placeId(2)
                .build();

        doReturn(goods)
                .when(goodsService).findGoods(id);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/goods/{id}", id)
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
        Goods goods = Goods.builder()
                .id(id)
                .genreName("서울")
                .title("지킬앤하이드")
                .desc("")
                .performanceStartDate(LocalDate.parse("2024-11-30", formatter))
                .performanceEndDate(LocalDate.parse("2025-01-30", formatter))
                .performanceTime("1900")
                .location("샤롯데")
                .x("")
                .y("")
                .genreId(1)
                .placeId(2)
                .build();
        String goodsJson = objectMapper.writeValueAsString(goods);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/goods/add")
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
        Goods goods = Goods.builder()
                .id(id)
                .genreName("서울")
                .title("지킬앤하이드")
                .desc("")
                .performanceStartDate(LocalDate.parse("2024-11-30", formatter))
                .performanceEndDate(LocalDate.parse("2025-01-30", formatter))
                .performanceTime("1900")
                .location("샤롯데")
                .x("")
                .y("")
                .genreId(1)
                .placeId(2)
                .build();
        String goodsJson = objectMapper.writeValueAsString(goods);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch("/v1/goods/modify")
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
        Goods goods = Goods.builder()
                .id(id)
                .genreName("서울")
                .title("지킬앤하이드")
                .desc("")
                .performanceStartDate(LocalDate.parse("2024-11-30", formatter))
                .performanceEndDate(LocalDate.parse("2025-01-30", formatter))
                .performanceTime("1900")
                .location("샤롯데")
                .x("")
                .y("")
                .genreId(1)
                .placeId(2)
                .build();
        String goodsJson = objectMapper.writeValueAsString(goods);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/v1/goods/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goodsJson));

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();
    }

    private List<Goods> createGoodsList() {
        return Arrays.asList(
                Goods.builder()
                        .id(1)
                        .genreName("서울")
                        .title("지킬앤하이드")
                        .desc("")
                        .performanceStartDate(LocalDate.parse("2024-11-30", formatter))
                        .performanceEndDate(LocalDate.parse("2025-01-30", formatter))
                        .performanceTime("1900")
                        .location("샤롯데")
                        .x("")
                        .y("")
                        .genreId(1)
                        .placeId(2)
                        .build()
                , Goods.builder()
                        .id(2)
                        .genreName("경기")
                        .title("웃는남자")
                        .desc("")
                        .performanceStartDate(LocalDate.parse("2024-11-30", formatter))
                        .performanceEndDate(LocalDate.parse("2025-01-30", formatter))
                        .performanceTime("1900")
                        .location("샤롯데")
                        .x("")
                        .y("")
                        .genreId(1)
                        .placeId(2)
                        .build()
        );
    }
}