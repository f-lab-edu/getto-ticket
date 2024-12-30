package com.flab.gettoticket.service;

public interface RedisCachingServie {
    void addSeatWarmUpData(long goodsId, long playId);
}
