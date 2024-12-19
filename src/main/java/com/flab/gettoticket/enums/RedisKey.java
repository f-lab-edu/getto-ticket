package com.flab.gettoticket.enums;

public enum RedisKey {
    // 대기열 key
    WAITING_KEY("waiting:goods::", "waiting"),

    // 처리열 key
    PROCESSING_KEY("processing:goods::", "processing"),

    // Queue Mete key
    QUEUE_METE_KEY("meta:", ""),

    // Traffic
    TRAFFIC_KEY("traffic:goods::", ""),

    // Seat key
    SEAT_KEY("seat:goods::", ""),
    SEAT_SALE_YN_KEY("seat:saleYn::", "");

    private final String key;
    private final String metaType;

    RedisKey(String key, String metaType) {
        this.key = key;
        this.metaType = metaType;
    }

    public String getKey() {
        return key;
    }
    public String getMetaType() {
        return metaType;
    }
}
