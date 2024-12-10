package com.flab.gettoticket.controller;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.entity.Zone;
import com.flab.gettoticket.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seat")
@Slf4j
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // 좌석 조회
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Seat>>> seatList(@RequestParam long goodsId) {
        List<Seat> data = seatService.findSeatList(goodsId);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    // 좌석 가격 조회
    @GetMapping("/price/{id}")
    public ResponseEntity<ApiResponse<List<Zone>>> zonePrice(@PathVariable long id) {
        List<Zone> data = seatService.findZonePrice(id);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}
