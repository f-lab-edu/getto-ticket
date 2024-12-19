package com.flab.gettoticket.service;

import com.flab.gettoticket.dto.RedisTokenReponse;
import com.flab.gettoticket.dto.RedisTokenRequest;

import java.time.LocalDate;

public interface RedisTokenService {
    void testScheduling();
    String createPlainTextKey(long goodsId, long playtimeId);
    String encodeToken(String plainTextKey, String email);
    boolean validateToken(String rawToken, String encodedToken);
    RedisTokenReponse findToken(RedisTokenRequest redisTokenRequest);
}
