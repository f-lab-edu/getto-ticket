package com.flab.gettoticket.controller;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.dto.PlayTimeDTO;
import com.flab.gettoticket.entity.PlayTime;
import com.flab.gettoticket.service.PlayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/play-time")
@Slf4j
public class PlayController {
    private final PlayService playService;
    public PlayController(PlayService playService) {
        this.playService = playService;
    }

    /**
     * 공연일 리스트 조회
     * @param goodsId 상품 id
     * @param startDate yyyyMMdd
     * @param endDate yyyyMMdd
     * @return List
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<String>>> playAtList(
            @RequestParam long goodsId
            , @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate
            , @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate
    ) {
        List<String> data = playService.findPlayAtList(goodsId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    /**
     * 특정 공연일의 회차 리스트 조회
     * @param goodsId 상품 id
     * @param playAt 공연일
     * @return
     */
    @GetMapping("/order/list")
    public ResponseEntity<ApiResponse<List<PlayTime>>> playOrderList(
            @RequestParam long goodsId
            , @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate playAt
    ) {
        List<PlayTime> data = playService.findPlayOrder(goodsId, playAt);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    /**
     * 특정 공연일 n회차 정보
     *  1. 구역별 잔여 좌석 개수
     *  2. (예: 배우정보)
     */
    @GetMapping("/order")
    public ResponseEntity<ApiResponse<PlayTimeDTO>> playOrderInfo(
            @RequestParam long playTimeId
            , @RequestParam long goodsId
    ) {
        PlayTimeDTO data = playService.findPlayTimeDTO(playTimeId, goodsId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}
