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
        Goods expectedGoods = new Goods(1, "서울", "지킬앤하이드", "", LocalDate.parse("2024-11-30"), LocalDate.parse("2025-01-30"), "1900", "샤롯데", "", "", 1, 2);

        when(goodsRepository.selectGoods(id))
                .thenReturn(expectedGoods);

        //when
        Goods goods = goodsRepository.selectGoods(id);

        //then
        assertThat(goods).isNotNull();
        assertThat(goods.getId()).isEqualTo(expectedGoods.getId());
        assertThat(goods.getGenreName()).isEqualTo(expectedGoods.getGenreName());
        assertThat(goods).usingRecursiveComparison().isEqualTo(expectedGoods); // 전체 객체 비교
    }

    @Test
    @DisplayName("상품 등록")
    void addGoods(){
        //given
        Goods expectedGoods = new Goods(1, "서울", "지킬앤하이드", "", LocalDate.parse("2024-11-30"), LocalDate.parse("2025-01-30"), "1900", "샤롯데", "", "", 1, 2);

        when(goodsRepository.insertGoods(expectedGoods))
                .thenReturn(1);

        //when
        int result = goodsRepository.insertGoods(expectedGoods);

        //then
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 수정")
    void modifyGoods(){
        //given
        Goods expectedGoods = new Goods(1, "서울", "지킬앤하이드", "", LocalDate.parse("2024-11-30"), LocalDate.parse("2025-01-30"), "1900", "샤롯데", "", "", 1, 2);

        when(goodsRepository.updateGoods(expectedGoods))
                .thenReturn(1);

        //when
        int result = goodsRepository.updateGoods(expectedGoods);

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

    @Test
    @DisplayName("좌석 가격 조회")
    void findZonePrice(){
        //given
        long id = 1;

        when(goodsRepository.selectZonePrice(id))
                .thenReturn(createZoneList());

        //when
        List<Zone> list = goodsServiceImpl.findZonePrice(id);

        //then
        Assertions.assertThat(list)
                .hasSize(2)
                .extracting(Zone::getId, Zone::getName)
                .contains(
                        tuple(1L, "VIP")
                        , tuple(2L, "R")
                );
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