package com.flab.gettoticket.controller;

import com.flab.gettoticket.model.Goods;
import com.flab.gettoticket.model.Zone;
import com.flab.gettoticket.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class GoodsController {
    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    // 상품 조회
    @GetMapping("/goods")
    public List<Goods> findGoodsList(@RequestParam int startIndex, @RequestParam int amount) {
        return goodsService.findGoodsList(startIndex, amount);
    }

    // 상품 상세 조회
    @GetMapping("/goods/{id}")
    public Goods findGoods(@PathVariable String id) {
        return goodsService.findGoods(id);
    }

    // 상품 등록
    @PostMapping("/goods")
    public String addGoods(@RequestBody Goods goods) {
        String result = "상품 추가에 성공하였습니다.";

        try {
            goodsService.addGoods(goods);
        } catch(Exception e) {
            result = "상품 추가에 실패하였습니다.";
            e.printStackTrace();
        }

        return result;
    }

    // 상품 수정
    @PatchMapping("/goods")
    public String modifyGoods(@RequestBody Goods goods) {
        String result = "상품 수정에 성공하였습니다.";

        try {
            goodsService.modifyGoods(goods);
        } catch(Exception e) {
            result = "상품 수정에 실패하였습니다.";
            e.printStackTrace();
        }

        return result;
    }

    // 상품 삭제
    @DeleteMapping("/goods/{goodsId}")
    public String removeGoods(@PathVariable String goodsId) {
        String result = "상품 삭제에 성공하였습니다.";

        try {
            goodsService.removeGoods(goodsId);
        } catch(Exception e) {
            result = "상품 삭제에 실패하였습니다.";
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 좌석 가격 조회
     * @param goodsId 상품 id
     * @return
     */
    @GetMapping("/goods/zone/{goodsId}")
    public List<Zone> zonePrice(@PathVariable String goodsId) {
        return goodsService.findZonePrice(goodsId);
    }
}
