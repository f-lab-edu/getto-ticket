package com.flab.gettoticket.repository;

import java.util.List;
import java.util.Map;

public interface RedisProcessingRepository {
    Map<String, String> selectProcessingQueueAll(String plainTextKey);
    long selectProcessingQueueSize(String plainTextKey);
    int selectProcessingQueue(String plainTextKey, long userSeq);
    boolean hasKey(String plainTextKey, long userSeq);
    void insertProcessingQueue(String plainTextKey, long userSeq, int status);
    void updateProcessingQueue(String plainTextKey, long userSeq, int status);
    void removeProcessingQueue(String plainTextKey, long userSeq);
}
