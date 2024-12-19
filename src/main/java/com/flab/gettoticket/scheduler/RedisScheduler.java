package com.flab.gettoticket.scheduler;

import com.flab.gettoticket.enums.QueueStatus;
import com.flab.gettoticket.enums.RedisKey;
import com.flab.gettoticket.repository.RedisMetaRepository;
import com.flab.gettoticket.repository.RedisProcessingRepository;
import com.flab.gettoticket.repository.RedisTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisScheduler {

    private final RedisTransactionRepository redisTransactionRepository;
    private final RedisProcessingRepository redisProcessingRepository;
    private final RedisMetaRepository redisMetaRepository;

    public static final long processingCapacity = 3L;    //TODO 별도 관리

    @Scheduled(cron = "*/10 * * * * *")
    public void waitToProcessQueueList() {
        Map<String, String> processingQueueMeta = redisMetaRepository.selectQueueMetaInfo(RedisKey.PROCESSING_KEY.getMetaType());
        log.info("processingQueueMeta {}", processingQueueMeta);

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
                        String plainTextKey = String.valueOf(goodsId + ":" + playTimeId);

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
                        long amount = (processingCapacity - currentSize) - 1;

                        log.info("처리열 최대 수용개수, 현재개수, 진입가능개수 capacity: {}, currentSize: {}, amount: {}", processingCapacity, currentSize, amount);

                        if(amount >= 0) {
                            redisTransactionRepository.schedulingQueue(plainTextKey, metaInfo, amount);  //amount 개수만큼 대기열에서 꺼내서 처리열에 진입
                        }
                    });
        }
    }
}
