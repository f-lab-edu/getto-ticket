package com.flab.gettoticket.controller;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.dto.SeatDTO;
import com.flab.gettoticket.service.PlayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/play-time")
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
     * 특정 공연일 n회차의 구역별 잔여 좌석 개수
     * @param playTimeId n회차의 공연시간 id
     * @return
     */
    @GetMapping("/order/{playTimeId}")
    public ResponseEntity<ApiResponse<List<SeatCountDTO>>> playOrderInfo(@PathVariable long playTimeId) {
        List<SeatCountDTO> data = playService.findSeatCount(playTimeId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    /**
     * 특정 공연일 첫번째 회차의 구역별 잔여 좌석 개수
     * @param goodsId 상품 id
     * @param playAt 공연일(yyyyMMdd)
     * @return Object
     */
    @GetMapping("/order/first")
    public ResponseEntity<ApiResponse<SeatDTO>> firstPlayOrderInfo(
            @RequestParam long goodsId
            , @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate playAt
    ) {
        SeatDTO data = playService.findSeatDTO(goodsId, playAt);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

}
