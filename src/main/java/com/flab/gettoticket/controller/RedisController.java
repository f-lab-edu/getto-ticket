package com.flab.gettoticket.controller;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.common.ApiResponseCode;
import com.flab.gettoticket.dto.RedisEntry;
import com.flab.gettoticket.repository.RedisCommRepository;
import com.flab.gettoticket.repository.RedisTransactionRepository;
import com.flab.gettoticket.service.RedisCommService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/v1/redis")
public class RedisController {

    private final RedisCommService RedisCommService;

    @GetMapping("/flushAllKey")
    public ResponseEntity<ApiResponse<Void>> removeFlushAllKey() {
        RedisCommService.flushAllKey();

        String message = "Redis 모든 Key 삭제 완료";

        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), message));
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addRedisKey(@RequestBody RedisEntry redisEntry) {
        String key = redisEntry.getKey();
        String value = redisEntry.getValue();

        RedisCommService.setData(key, value);

        String message = "Redis Key/Value 추가 성공";

        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), message));
    }

    @GetMapping("/{key}")
    public ResponseEntity<ApiResponse<String>> getRedisKey(@PathVariable String key) {
        String value = RedisCommService.getData(key);

        String message = value != null ? "Redis Key/Value 조회 성공" : "해당 키에 대한 데이터가 없습니다.";

        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), message, value));
    }

    @PostMapping("/remove")
    public ResponseEntity<ApiResponse<Void>> removeRedisKey(@RequestBody RedisEntry redisEntry) {
        String key = redisEntry.getKey();

        RedisCommService.deleteData(key);

        String message = "Redis Key/Value 삭제 성공";

        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), message));
    }
}
