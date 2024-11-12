package com.flab.gettoticket.service;

import com.flab.gettoticket.model.Goods;
import com.flab.gettoticket.model.Zone;

import java.util.List;

public interface GoodsService {
    List<Goods> findGoodsList(int startIndex, int amount);
    Goods findGoods(String goodsId);
    void addGoods(Goods goods);
    void modifyGoods(Goods goods);
    void removeGoods(String goodsId);
    List<Zone> findZonePrice(String goodsId);
}
