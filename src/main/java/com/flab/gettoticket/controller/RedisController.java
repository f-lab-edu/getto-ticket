package com.flab.gettoticket.controller;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.common.ApiResponseCode;
import com.flab.gettoticket.dto.RedisEntry;
import com.flab.gettoticket.repository.RedisRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private final RedisRepository redisRepository;

    public RedisController(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addRedisKey(@RequestBody RedisEntry redisEntry) {
        String key = redisEntry.getKey();
        Object value = redisEntry.getValue();

        redisRepository.setData(key, value);

        String message = "Redis Key/Value 추가 성공";

        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), message));
    }

    @GetMapping("/{key}")
    public ResponseEntity<ApiResponse<Object>> getRedisKey(@PathVariable String key) {
        Object value = redisRepository.getData(key);

        String message = value != null ? "Redis Key/Value 조회 성공" : "해당 키에 대한 데이터가 없습니다.";

        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), message, value));
    }

    @PostMapping("/remove")
    public ResponseEntity<ApiResponse<Void>> removeRedisKey(@RequestBody RedisEntry redisEntry) {
        String key = redisEntry.getKey();

        redisRepository.deleteData(key);

        String message = "Redis Key/Value 삭제 성공";

        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), message));
    }
}
