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

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;

    public GoodsServiceImpl(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @Override
    public List<Goods> findGoodsList(Pageable pageable) {
        List<Goods> list = new ArrayList<>();
        int limit = 0;
        long offset = 0L;

        try {
            limit = pageable.getPageSize();
            offset = pageable.getOffset();

            list = goodsRepository.selectGoodsList(limit, offset);
        } catch (Exception e) {
            log.error("상품 조회 중 예외 발생 limit: {}, offset: {}, Error: {}", limit, offset, e.getMessage(), e);
            throw new RuntimeException("상품 조회에 실패했습니다.", e);
        }

        return list;
    }

    @Override
    public Goods findGoods(long id) {
        Goods goods = new Goods();
        try {
            goods = goodsRepository.selectGoods(id);
        } catch (Exception e) {
            log.error("상품 상세 조회 중 예외 발생 id: {}, Error: {}", id, e.getMessage(), e);
            throw new RuntimeException("상품 상세 조회에 실패했습니다.", e);
        }
        return goods;
    }

    @Override
    public void addGoods(Goods goods) {
        try {
            goodsRepository.insertGoods(goods);
        } catch (Exception e) {
            log.error("상품 등록 중 예외 발생 goods: {}, Error: {}", goods, e.getMessage(), e);
            throw new RuntimeException("상품 등록에 실패했습니다.", e);
        }
    }

    @Override
    public void modifyGoods(Goods goods) {
        try {
            goodsRepository.updateGoods(goods);
        } catch (Exception e) {
            log.error("상품 수정 중 예외 발생 goods: {}, Error: {}", goods, e.getMessage(), e);
            throw new RuntimeException("상품 수정에 실패했습니다.", e);
        }
    }

    @Override
    public void removeGoods(Goods goods) {
        long id = 0L;
        try {
            id = goods.getId();
            goodsRepository.deleteGoods(id);
        } catch (Exception e) {
            log.error("상품 삭제 중 예외 발생 id: {}, Error: {}", id, e.getMessage(), e);
            throw new RuntimeException("상품 삭제에 실패했습니다.", e);
        }
    }

    @Override
    public List<Zone> findZonePrice(long id) {
        List<Zone> list = null;

        try {
            list = goodsRepository.selectZonePrice(id);
        } catch (Exception e) {
            log.error("좌석 가격 조회 중 예외 발생 id: {}, Error: {}", id, e.getMessage(), e);
            throw new RuntimeException("좌석 가격 조회에 실패하였습니다.", e);
        }
        return list;
    }
}
