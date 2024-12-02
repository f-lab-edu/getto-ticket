package com.flab.gettoticket.repository;

import com.flab.gettoticket.entity.Goods;
import com.flab.gettoticket.entity.Zone;

import java.util.List;

public interface GoodsRepository {
    List<Goods> selectGoodsList(int limit, long offset);
    Goods selectGoods(long id);
    int insertGoods(Goods goods);
    int updateGoods(Goods goods);
    int deleteGoods(long id);
    List<Zone> selectZonePrice(long id);
}
