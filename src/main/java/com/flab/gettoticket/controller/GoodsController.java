package com.flab.gettoticket.controller;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.common.ApiResponseCode;
import com.flab.gettoticket.entity.Goods;
import com.flab.gettoticket.entity.Zone;
import com.flab.gettoticket.service.GoodsService;
import com.flab.gettoticket.util.PageRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/goods")
@Slf4j
public class GoodsController {
    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    // 상품 조회
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Goods>>> findGoodsList(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequestUtil.of(page, size);
        List<Goods> data = goodsService.findGoodsList(pageable);

        log.info(data.toString());

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    // 상품 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Goods>> findGoods(@PathVariable long id) {
        Goods data = goodsService.findGoods(id);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    // 상품 등록
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addGoods(@RequestBody Goods goods) {
        goodsService.addGoods(goods);
        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), "상품 추가에 성공하였습니다."));
    }

    // 상품 수정
    @PatchMapping("/modify")
    public ResponseEntity<ApiResponse<Void>> modifyGoods(@RequestBody Goods goods) {
        goodsService.modifyGoods(goods);
        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), "상품 수정에 성공하였습니다."));
    }

    // 상품 삭제
    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Void>> removeGoods(@RequestBody Goods goods) {
        goodsService.removeGoods(goods);
        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), "상품 삭제에 성공하였습니다."));
    }
}
