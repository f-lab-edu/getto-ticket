package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.entity.Goods;
import com.flab.gettoticket.entity.Zone;
import com.flab.gettoticket.repository.GoodsRepository;
import com.flab.gettoticket.util.PageRequestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoodsServiceImplTest {
    @InjectMocks
    private GoodsServiceImpl goodsServiceImpl;

    @Mock
    private GoodsRepository goodsRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    @DisplayName("상품 조회")
    void findGoodsList(){
        //given
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequestUtil.of(page, size);

        int limit = PageRequestUtil.getLimit(pageable);
        long offset = PageRequestUtil.getOffset(pageable);

        when(goodsRepository.selectGoodsList(limit, offset))
                .thenReturn(createGoodsList());

        //when
        List<Goods> list = goodsServiceImpl.findGoodsList(pageable);

        //then
        Assertions.assertThat(list)
                .hasSize(2)
                .extracting(Goods::getId, Goods::getGenreName)
                .contains(
                        tuple(1L, "서울")
                        , tuple(2L, "경기")
                );
    }

    @Test
    @DisplayName("상품 상세 조회")
    void findGoods(){
        //given
        long id = 1;
        Goods expectedGoods = Goods.builder()
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

        when(goodsRepository.selectGoods(id))
                .thenReturn(expectedGoods);

        //when
        Goods goods = goodsRepository.selectGoods(id);

        //then
        assertThat(goods).isNotNull();
        assertThat(goods.getId()).isEqualTo(goods.getId());
        assertThat(goods.getGenreName()).isEqualTo(goods.getGenreName());
        assertThat(goods).usingRecursiveComparison().isEqualTo(goods); // 전체 객체 비교
    }

    @Test
    @DisplayName("상품 등록")
    void addGoods(){
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

        when(goodsRepository.insertGoods(goods))
                .thenReturn(1);

        //when
        int result = goodsRepository.insertGoods(goods);

        //then
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 수정")
    void modifyGoods(){
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

        when(goodsRepository.updateGoods(goods))
                .thenReturn(1);

        //when
        int result = goodsRepository.updateGoods(goods);

        //then
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 삭제")
    void removeGoods(){
        //given
        long id = 1;

        when(goodsRepository.deleteGoods(id))
                .thenReturn(1);

        //when
        int result = goodsRepository.deleteGoods(id);

        //then
        assertThat(result).isEqualTo(1);
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