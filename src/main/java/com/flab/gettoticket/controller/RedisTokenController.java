package com.flab.gettoticket.controller;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.common.ApiResponseCode;
import com.flab.gettoticket.dto.RedisTokenReponse;
import com.flab.gettoticket.dto.RedisTokenRequest;
import com.flab.gettoticket.service.MonitoringService;
import com.flab.gettoticket.service.RedisCachingServie;
import com.flab.gettoticket.service.RedisTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/token")
@Slf4j
public class RedisTokenController {

    private final RedisTokenService redisTokenService;
    private final RedisCachingServie redisCachingServie;
    private final MonitoringService monitoringService;

    @GetMapping("/test/scheduling")
    public ResponseEntity<ApiResponse<Void>> testScheduling() {
        redisTokenService.testScheduling();
        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), "대기열에서 처리열로 진입 완료"));
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<RedisTokenReponse>> findToken(HttpServletRequest request, @RequestBody RedisTokenRequest redisTokenRequest) {
        String token = request.getHeader("authorization");
        long goodsId = redisTokenRequest.getGoodsId();
        long playTimeId = redisTokenRequest.getPlayTimeId();

//        //트래픽 임계치 초과시
//        if(monitoringService.createWaitQueue(goodsId, playTimeId)) { //좌석 캐싱 }

        //좌석 캐싱
        redisCachingServie.addSeatWarmUpData(goodsId, playTimeId);

        RedisTokenReponse data = redisTokenService.findToken(RedisTokenRequest.builder()
                        .email(redisTokenRequest.getEmail())
                        .goodsId(redisTokenRequest.getGoodsId())
                        .playTimeId(redisTokenRequest.getPlayTimeId())
                        .playAt(redisTokenRequest.getPlayAt())
                        .token(token)
                        .build());

        return ResponseEntity.ok((ApiResponse.ok(data)));
    }
}
