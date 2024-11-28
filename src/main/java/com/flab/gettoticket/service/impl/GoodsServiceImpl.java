package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.model.Goods;
import com.flab.gettoticket.model.Zone;
import com.flab.gettoticket.repository.GoodsRepository;
import com.flab.gettoticket.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;

    public GoodsServiceImpl(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @Override
    public List<Goods> findGoodsList(Pageable pageable) {
        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();

        List<Goods> list = goodsRepository.selectGoodsList(limit, offset);

        if(Objects.isNull(list)) {
            log.error("상품 조회 중 예외 발생 limit: {}, offset: {}", limit, offset);
            throw new RuntimeException("상품 조회에 실패했습니다.");
        }

        return list;
    }

    @Override
    public Goods findGoods(long id) {
        Goods goods = goodsRepository.selectGoods(id);

        if(Objects.isNull(goods)) {
            log.error("상품 상세 조회 중 예외 발생 id: {}", id);
            throw new RuntimeException("상품 상세 조회에 실패했습니다.");
        }

        return goods;
    }

    @Override
    public void addGoods(Goods goods) {
        int result = goodsRepository.insertGoods(goods);

        if(result == 0) {
            log.error("상품 등록 중 예외 발생 goods: {}", goods);
            throw new RuntimeException("상품 등록에 실패했습니다.");
        }
    }

    @Override
    public void modifyGoods(Goods goods) {
        int result = goodsRepository.updateGoods(goods);

        if(result == 0) {
            log.error("상품 수정 중 예외 발생 goods: {}", goods);
            throw new RuntimeException("상품 수정에 실패했습니다.");
        }
    }

    @Override
    public void removeGoods(Goods goods) {
        long id = goods.getId();
        int result = goodsRepository.deleteGoods(id);

        if(result == 0) {
            log.error("상품 삭제 중 예외 발생 id: {}", id);
            throw new RuntimeException("상품 삭제에 실패했습니다.");
        }
    }

    @Override
    public List<Zone> findZonePrice(long id) {
        List<Zone> list = goodsRepository.selectZonePrice(id);

        if(Objects.isNull(list)) {
            log.error("좌석 가격 조회 중 예외 발생 id: {}", id);
            throw new RuntimeException("좌석 가격 조회에 실패하였습니다.");
        }

        return list;
    }
}