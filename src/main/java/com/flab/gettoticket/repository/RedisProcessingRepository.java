package com.flab.gettoticket.repository;

import java.util.List;
import java.util.Map;

public interface RedisProcessingRepository {
    Map<String, String> selectProcessingQueueAll(String plainTextKey);
    long selectProcessingQueueSize(String plainTextKey);
    void insertProcessingQueue(String plainTextKey, String token, String status);
    void updateProcessingQueue(String plainTextKey, String token, String status);
    void removeProcessingQueue(String plainTextKey, String token);
}
