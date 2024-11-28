package com.flab.gettoticket.service;

import com.flab.gettoticket.model.Goods;
import com.flab.gettoticket.model.Zone;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GoodsService {
    List<Goods> findGoodsList(Pageable pageable);
    Goods findGoods(long id);
    void addGoods(Goods goods);
    void modifyGoods(Goods goods);
    void removeGoods(Goods goods);
    List<Zone> findZonePrice(long id);
}
