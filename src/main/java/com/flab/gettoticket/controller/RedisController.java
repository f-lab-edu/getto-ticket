package com.flab.gettoticket.controller;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.common.ApiResponseCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class RedisController {

//    private final RedisRepository redisRepository;    //TODO

    private final RedisTemplate<String, String> redisTemplate;

    public RedisController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addRedisKey() {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        vop.set("seat:123", "1열32");
        vop.set("seat:234", "1열33");

        String message = "Redis Object 추가 성공";

        return ResponseEntity.ok(ApiResponse.create(ApiResponseCode.SUCCESS.getCode(), message));
    }

    @GetMapping("/{key}")
    public ResponseEntity<ApiResponse<String>> getRedisKey(@PathVariable String key) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String value = vop.get(key);

        return ResponseEntity.ok(ApiResponse.ok(value));
    }

}
