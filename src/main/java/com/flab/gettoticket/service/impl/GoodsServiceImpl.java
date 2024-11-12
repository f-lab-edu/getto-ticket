package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.model.Goods;
import com.flab.gettoticket.model.Zone;
import com.flab.gettoticket.repository.GoodsRepository;
import com.flab.gettoticket.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;

    public GoodsServiceImpl(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @Override
    public List<Goods> findGoodsList(int startIndex, int amount) {
        return goodsRepository.selectGoodsList(startIndex, amount);
    }

    @Override
    public Goods findGoods(String goodsId) {
        return goodsRepository.selectGoods(goodsId);
    }

    @Override
    public void addGoods(Goods goods) {
        goodsRepository.insertGoods(goods);
    }

    @Override
    public void modifyGoods(Goods goods) {
        goodsRepository.updateGoods(goods);
    }

    @Override
    public void removeGoods(String goodsId) {
        goodsRepository.deleteGoods(goodsId);
    }

    @Override
    public List<Zone> findZonePrice(String goodsId) {
        return goodsRepository.selectZonePrice(goodsId);
    }
}
