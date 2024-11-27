package com.flab.gettoticket.repository;

import com.flab.gettoticket.model.Goods;
import com.flab.gettoticket.model.Zone;

import java.util.List;

public interface GoodsRepository {
    List<Goods> selectGoodsList(int limit, long offset);
    Goods selectGoods(long id);
    int insertGoods(Goods goods);
    int updateGoods(Goods goods);
    int deleteGoods(long id);
    List<Zone> selectZonePrice(long id);
}
