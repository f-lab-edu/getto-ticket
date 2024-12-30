package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.dto.RedisTokenReponse;
import com.flab.gettoticket.dto.RedisTokenRequest;
import com.flab.gettoticket.enums.QueueStatus;
import com.flab.gettoticket.enums.RedisKey;
import com.flab.gettoticket.repository.RedisMetaRepository;
import com.flab.gettoticket.repository.RedisProcessingRepository;
import com.flab.gettoticket.repository.RedisTransactionRepository;
import com.flab.gettoticket.repository.RedisWaitingRepository;
import com.flab.gettoticket.scheduler.RedisScheduler;
import com.flab.gettoticket.service.RedisTokenService;
import com.flab.gettoticket.util.TimeUnitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisTokenServiceImpl implements RedisTokenService {
    private final RedisWaitingRepository redisWaitingRepository;
    private final RedisProcessingRepository redisProcessingRepository;
    private final RedisTransactionRepository redisTransactionRepository;
    private final RedisMetaRepository redisMetaRepository;
    private final PasswordEncoder passwordEncoder;
    private final long capacity = RedisScheduler.processingCapacity;

    @Override
    public String createPlainTextKey(long goodsId, long playTimeId) {
        return String.valueOf(goodsId + ":" + playTimeId);
    }

    @Override
    public String encodeToken(String plainTextKey, String email) {
        String rawToken = plainTextKey + ":" + email;

        return passwordEncoder.encode(rawToken);
    }

    @Override
    public boolean validateToken(String rawToken, String encodedToken) {
        return passwordEncoder.matches(rawToken, encodedToken);
    }

    @Override
    public RedisTokenReponse findToken(RedisTokenRequest redisTokenRequest) {
        long userSeq = redisTokenRequest.getUserSeq();
        String email = redisTokenRequest.getEmail();
        long goodsId = redisTokenRequest.getGoodsId();
        long playTimeId = redisTokenRequest.getPlayTimeId();
        LocalDate playAt = redisTokenRequest.getPlayAt();

        String plainTextKey = createPlainTextKey(goodsId, playTimeId);
        long playDateTime = TimeUnitUtil.getLocalDateToMillSec(playAt);
        String metaInfo = "goodsId:" + goodsId + ", playTimeId:" + playTimeId + ", userSeq:" + userSeq + ", playDateTime:" + playDateTime;

        boolean hasProcessingKey = redisProcessingRepository.hasKey(plainTextKey, userSeq);
        long rank = redisWaitingRepository.selectWaitingRank(plainTextKey, userSeq);
        String status = "";
        String reqDateTime = "";
        long score = 0L;

        log.info("rank: {}, hasProcessingKey: {}", rank, hasProcessingKey);

        //생성된 대기열 토큰이 있다면
        if(rank > 0) {
            score = redisWaitingRepository.selectWaitingScore(plainTextKey, userSeq);
            status = QueueStatus.WAIT.getCode();
            reqDateTime = TimeUnitUtil.getMillisecondsToDateTime(score);

            return RedisTokenReponse.builder()
                    .userSeq(userSeq)
                    .email(email)
                    .goodsId(goodsId)
                    .playTimeId(playTimeId)
                    .rank(rank)
                    .status(status)
                    .reqDateTime(reqDateTime)
                    .build();
        }
        else if(hasProcessingKey) {
            status = redisProcessingRepository.selectProcessingQueue(plainTextKey, userSeq);

            return RedisTokenReponse.builder()
                    .userSeq(userSeq)
                    .email(email)
                    .goodsId(goodsId)
                    .playTimeId(playTimeId)
                    .rank(rank)
                    .status(status)
                    .reqDateTime(reqDateTime)
                    .build();
        }

        long currentSize = redisProcessingRepository.selectProcessingQueueSize(plainTextKey);

        if(currentSize < capacity) {
            status = QueueStatus.PROCESS.getCode();

            //처리열 진입
            redisTransactionRepository.waitToProcessQueue(plainTextKey, metaInfo, userSeq, currentSize);
        }
        else {
            status = QueueStatus.QUEUED.getCode();

            //대기열 진입
            score = redisTransactionRepository.activateWaitQueue(plainTextKey, metaInfo, userSeq);
            rank = redisWaitingRepository.selectWaitingRank(plainTextKey, userSeq);
            reqDateTime = TimeUnitUtil.getMillisecondsToDateTime(score);
        }

        return RedisTokenReponse.builder()
                .email(email)
                .goodsId(goodsId)
                .playTimeId(playTimeId)
                .rank(rank)
                .status(status)
                .reqDateTime(reqDateTime)
                .build();
    }
}
