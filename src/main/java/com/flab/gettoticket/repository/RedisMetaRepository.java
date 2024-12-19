package com.flab.gettoticket.repository;

import java.util.Map;
import java.util.Set;

public interface RedisMetaRepository {
    Map<String, String> selectQueueMetaInfo(String metaType);
    void insertQueueMetaInfo(String metaType, String queueKey, String metaInfo);
    void removeQueueMetaInfo(String metaType, String queueKey);
}
