package com.flab.gettoticket.controller;

import com.flab.gettoticket.dto.PlayAtDTO;
import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.dto.SeatDTO;
import com.flab.gettoticket.service.PlayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class PlayController {
    private final PlayService playService;
    public PlayController(PlayService playService) {
        this.playService = playService;
    }

    /**
     * 공연일 리스트 조회
     * @param goodsId 상품 id
     * @param startDate yyyymmdd
     * @param endDate yyyymmdd
     * @return List
     */
    @GetMapping("/play")
    public PlayAtDTO playAtList(@RequestParam String goodsId, @RequestParam String startDate, @RequestParam String endDate) {
        List<String> list = playService.findPlayAtList(goodsId, startDate, endDate);

        PlayAtDTO dto = new PlayAtDTO();
        dto.setPlayAt(list);

        return dto;
    }

    /**
     * 특정 공연일 n회차의 구역별 잔여 좌석 개수
     * @param playTimeId n회차의 공연시간 id
     * @return
     */
    @GetMapping("/play/order/{playTimeId}")
    public List<SeatCountDTO> playOrderInfo(@PathVariable String playTimeId) {
        return playService.findSeatCount(playTimeId);
    }

    /**
     * 특정 공연일 첫번째 회차의 구역별 잔여 좌석 개수
     * @param goodsId 상품 id
     * @param playAt 공연일(yyyymmddd)
     * @return Object
     */
    @GetMapping("/play/first")
    public SeatDTO firstPlayOrderInfo(@RequestParam String goodsId, @RequestParam String playAt) {
        return playService.findSeatDTO(goodsId, playAt);
    }

}
