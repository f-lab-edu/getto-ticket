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
    public void testScheduling() {
        Map<String, String> processingQueueMeta = redisMetaRepository.selectQueueMetaInfo(RedisKey.PROCESSING_KEY.getMetaType());
        log.info("processingQueueMeta: {}", processingQueueMeta);

        if(processingQueueMeta != null && !processingQueueMeta.isEmpty()) {
            processingQueueMeta.entrySet().stream()
                    .forEach(entry -> {
                        String value = entry.getValue();
                        long[] valueArr = Arrays.stream(value.split(","))
                                                .mapToLong(s -> Long.parseLong(s.split(":")[1]))
                                                .toArray();
                        long goodsId = valueArr[0];
                        long playTimeId = valueArr[1];
                        long playDateTime = valueArr[2];
                        String metaInfo = "goodsId:" + goodsId + ",playTimeId:" + playTimeId + ",playDateTime:" + playDateTime;
                        String plainTextKey = createPlainTextKey(goodsId, playTimeId);

                        //'처리 실패, 처리 완료'인 Queue 제거
                        Map<String, String> processingQueueAll = redisProcessingRepository.selectProcessingQueueAll(plainTextKey);

                        log.info("processingQueueAll: {}", processingQueueAll);

                        processingQueueAll.entrySet()
                                .stream()
                                .forEach(queue -> {
                                    String token = queue.getKey();
                                    String status = queue.getValue();
                                    String processingQueueKey = RedisKey.PROCESSING_KEY.getKey() + plainTextKey;

                                    if( !status.equals(QueueStatus.PROCESS.getCode()) && !status.equals(QueueStatus.RESERVED.getCode()) ) {
                                        redisProcessingRepository.removeProcessingQueue(plainTextKey, token);
                                        redisMetaRepository.removeQueueMetaInfo(RedisKey.PROCESSING_KEY.getMetaType(), processingQueueKey);
                                    }
                                });

                        //처리열 큐 관리 후, '처리중, 좌석 임시 예약' Queue 개수
                        long currentSize = redisProcessingRepository.selectProcessingQueueSize(plainTextKey);
                        long amount = (capacity - currentSize) - 1;

                        log.info("처리열 최대 수용개수, 현재개수, 진입가능개수 capacity: {}, currentSize: {}, amount: {}", capacity, currentSize, amount);

                        if(amount >= 0) {
                            redisTransactionRepository.schedulingQueue(plainTextKey, metaInfo, amount);  //amount 개수만큼 대기열에서 꺼내서 처리열에 진입
                        }
                    });
        }
    }

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
        String email = redisTokenRequest.getEmail();
        long goodsId = redisTokenRequest.getGoodsId();
        long playTimeId = redisTokenRequest.getPlayTimeId();
        LocalDate playAt = redisTokenRequest.getPlayAt();
        String token = redisTokenRequest.getToken();

        long rank = 0L;
        String status = "";
        String reqDateTime = "";
        long score = 0L;

        String plainTextKey = createPlainTextKey(goodsId, playTimeId);
        long playDateTime = TimeUnitUtil.getLocalDateToMillSec(playAt);
        String metaInfo = "goodsId:" + goodsId + ",playTimeId:" + playTimeId + ",playDateTime:" + playDateTime;

        //생성된 대기열 토큰이 있다면
        if(token != null) {
            log.info("대기열 토큰 존재 token: {}", token);

            score = redisWaitingRepository.selectWaitingScore(plainTextKey, token);
            rank = redisWaitingRepository.selectWaitingRank(plainTextKey, token);
            status = QueueStatus.WAIT.getCode();
            reqDateTime = TimeUnitUtil.getMillisecondsToDateTime(score);

            return RedisTokenReponse.builder()
                    .email(email)
                    .goodsId(goodsId)
                    .playTimeId(playTimeId)
                    .token(token)
                    .rank(rank)
                    .status(status)
                    .reqDateTime(reqDateTime)
                    .build();
        }

        //토큰 생성
        token = encodeToken(plainTextKey, email);

        log.info("대기열 토큰 생성 token: {}", token);

        long currentSize = redisProcessingRepository.selectProcessingQueueSize(plainTextKey);

        if(currentSize < capacity) {
            status = QueueStatus.PROCESS.getCode();

            //처리열 진입
            redisTransactionRepository.waitToProcessQueue(plainTextKey, metaInfo, token, currentSize);
        }
        else {
            status = QueueStatus.QUEUED.getCode();

            //대기열 진입
            score = redisTransactionRepository.activateWaitQueue(plainTextKey, metaInfo, token);
            rank = redisWaitingRepository.selectWaitingRank(plainTextKey, token);
            reqDateTime = TimeUnitUtil.getMillisecondsToDateTime(score);
        }

        return RedisTokenReponse.builder()
                .email(email)
                .goodsId(goodsId)
                .playTimeId(playTimeId)
                .token(token)
                .rank(rank)
                .status(status)
                .reqDateTime(reqDateTime)
                .build();
    }
}
