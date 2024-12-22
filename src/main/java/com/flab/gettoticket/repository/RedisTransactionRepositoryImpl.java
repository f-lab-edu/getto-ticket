package com.flab.gettoticket.repository;

import com.flab.gettoticket.enums.QueueStatus;
import com.flab.gettoticket.enums.RedisKey;
import com.flab.gettoticket.util.TimeUnitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisTransactionRepositoryImpl implements RedisTransactionRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisWaitingRepository redisWaitingRepository;
    private final RedisProcessingRepository redisProcessingRepository;
    private final RedisMetaRepository redisMetaRepository;

    /**
     * 스케줄링, 공연+회차별 대기열과 처리열 관리
     * @param plainTextKey 공연id + 공연일시id (goodsId + playTimeId)
     * @param metaInfo
     * @param amount
     */
    @Override
    public void schedulingQueue(String plainTextKey, String metaInfo, long amount) {
        String waitingQueueKey = RedisKey.WAITING_KEY.getKey() + plainTextKey;
        String processingQueueKey = RedisKey.PROCESSING_KEY.getKey() + plainTextKey;

        //대기열 조회 및 삭제
        Set<ZSetOperations.TypedTuple<String>> waitingQueue = redisWaitingRepository.selectRangeByScore(plainTextKey, 0, amount);

        log.info("waitingQueue: {}", waitingQueue);

        if (waitingQueue != null && !waitingQueue.isEmpty()) {
            redisTemplate.execute((RedisCallback<String>) connection -> {
                connection.multi();

                    waitingQueue.forEach(queue -> {
                        String seq = queue.getValue();
                        long userSeq = Long.parseLong(seq);
                        String status = QueueStatus.PROCESS.getCode();

                        redisProcessingRepository.insertProcessingQueue(plainTextKey, userSeq, status);   //처리열 추가
                        redisWaitingRepository.removeWaitingQueue(plainTextKey, userSeq);                 //대기열 삭제
                        redisMetaRepository.removeQueueMetaInfo(RedisKey.WAITING_KEY.getMetaType(), waitingQueueKey);   //대기열 메타 데이터 삭제
                    });

                    redisMetaRepository.insertQueueMetaInfo(RedisKey.PROCESSING_KEY.getMetaType(), processingQueueKey, metaInfo);

                connection.exec();  //commit
                return null;
            });
        }
    }

    /**
     *
     * @param plainTextKey 공연id + 공연일시id (goodsId + playTimeId)
     * @param metaInfo token 생성한 평문 문자열
     * @param userSeq
     * @return
     */
    @Override
    public long activateWaitQueue(String plainTextKey, String metaInfo, long userSeq) {
        //대기열 진입
        String waitingQueueKey = RedisKey.WAITING_KEY.getKey() + plainTextKey;
        long score = redisWaitingRepository.insertWaitingQueue(plainTextKey, userSeq);

        //대기열 메타정보 추가
        redisMetaRepository.insertQueueMetaInfo(RedisKey.WAITING_KEY.getMetaType(), waitingQueueKey, metaInfo);

        return score;
    }

    @Override
    public void waitToProcessQueue(String plainTextKey, String metaInfo, long userSeq, long queueSize) {
        redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.multi();

            String processingQueueKey = RedisKey.PROCESSING_KEY.getKey() + plainTextKey;

            redisWaitingRepository.removeWaitingQueue(plainTextKey, userSeq);                                                 //대기열 삭제
            redisProcessingRepository.insertProcessingQueue(plainTextKey, userSeq, QueueStatus.PROCESS.getCode());            //처리열 추가
            redisMetaRepository.insertQueueMetaInfo(RedisKey.PROCESSING_KEY.getMetaType(), processingQueueKey, metaInfo);   //처리열 메타정보 추가

            //processingQueueKey 에 대한 처리열 최초 생성시
            if(queueSize == 0) {
                long[] valueArr = Arrays.stream(metaInfo.split(","))
                                        .mapToLong(s -> Long.parseLong(s.split(":")[1]))
                                        .toArray();
                long playDateTime = valueArr[3];
                long keyExpireDuration = TimeUnitUtil.getCurrDateTimeDiff(playDateTime);      //'playDateTime - 현재 시간' 까지 key 유효시간 설정

                redisTemplate.expire(processingQueueKey, keyExpireDuration, TimeUnit.SECONDS);

                log.info("TTL 설정 - processingQueueKey: {}, keyExpireDuration: {}", processingQueueKey, keyExpireDuration);
            }

            connection.exec();
            return null;
        });
    }
}
