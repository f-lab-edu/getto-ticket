package com.flab.gettoticket.repository;

import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.enums.RedisKey;
import com.flab.gettoticket.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class RedisSeatRepositoryImpl implements RedisSeatRepository{
    private final RedisTemplate<String, Seat> redisTemplate;
    private final HashOperations<String,String,String> hashOps;

    public RedisSeatRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOps = redisTemplate.opsForHash();
    }

    private final int DEFAULT_EXPIRE_SECONDS = 300; //5m TODO 별도 관리


    @Override
    public List<Seat> selectSeatAll(String plainTextKey) {
        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        List<String> jsonList = hashOps.values(key);
        List<Seat> list = new ArrayList<>();

        for(String json : jsonList) {
            Seat seat = ObjectMapperUtil.fromJson(json, Seat.class);
            list.add(seat);
        }

        return list;
    }

    @Override
    public long selectSeatQueueSize(String plainTextKey) {
        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        long size = hashOps.size(key);

        log.info("Seat field 개수 조회 key: {}, size: {}", key, size);

        return size;
    }

    @Override
    public String selectSaleYn(String plainTextKey, long seatId) {
        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        String field = String.valueOf(seatId);

        return hashOps.get(key, field);
    }

    @Override
    public void insertSeatInfo(long goodsId, long playTimeId, Seat seat) {
        long seatId = seat.getId();
        String plainTextKey = String.valueOf(goodsId + ":" + playTimeId);

        //SEAT_KEY
        String keySeat = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        String fieldSeat = String.valueOf(seatId);

        //SEAT_SALE_YN_KEY
        String key = RedisKey.SEAT_SALE_YN_KEY.getKey() + plainTextKey;
        String field = String.valueOf(seatId);
        String saleYn = String.valueOf(seat.getSaleYn());

        String toJson = ObjectMapperUtil.toJson(seat);

        hashOps.put(keySeat, fieldSeat, toJson);
        hashOps.put(key, field, saleYn);

        log.info("Seat field 추가 key: {}, seatId: {}, seatToJson: {}", keySeat, fieldSeat, toJson);
        log.info("Seat saleYn field 추가 key: {}, seatId:saleYn: {}, saleYn: {}", key, seatId, saleYn);
    }

    @Override
    public void deleteSeatInfo(String plainTextKey, long seatId) {
        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        String field = String.valueOf(seatId);

        hashOps.delete(key, field);
    }
}
