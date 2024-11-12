package com.flab.gettoticket.repository;

import com.flab.gettoticket.model.Goods;
import com.flab.gettoticket.model.Zone;

import java.util.List;

public interface GoodsRepository {
    List<Goods> selectGoodsList(int startIndex, int amount);
    Goods selectGoods(String goodsId);
    void insertGoods(Goods goods);
    void updateGoods(Goods goods);
    void deleteGoods(String goodsId);
    List<Zone> selectZonePrice(String goodsId);
}
