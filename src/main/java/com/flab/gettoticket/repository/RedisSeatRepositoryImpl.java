package com.flab.gettoticket.repository;

import com.flab.gettoticket.entity.Seat;
import com.flab.gettoticket.enums.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class RedisSeatRepositoryImpl implements RedisSeatRepository{
    private final RedisTemplate<String, Seat> seatRedisTemplate;
    private final HashOperations<String,String,Seat> hashOps;

    public RedisSeatRepositoryImpl(RedisTemplate<String, Seat> redisTemplate) {
        this.seatRedisTemplate = redisTemplate;
        this.hashOps = redisTemplate.opsForHash();
    }

    @Override
    public List<Seat> selectSeatAll(String plainTextKey) {
        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        return hashOps.values(key);
    }

    @Override
    public Seat selectSeat(String plainTextKey, long seatId) {
        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        String field = String.valueOf(seatId);

        return hashOps.get(key, field);
    }

    @Override
    public long selectSeatQueueSize(String plainTextKey) {
        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        long size = hashOps.size(key);

        log.info("Seat field 개수 조회 key: {}, size: {}", key, size);

        return size;
    }

    @Override
    public void insertSeatInfo(String plainTextKey, Seat seat) {
        long seatId = seat.getId();

        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        String field = String.valueOf(seatId);

        hashOps.put(key, field, seat);

        log.info("Seat field 추가 key: {}, seatId: {}, Seat: {}", key, field, seat);
    }

    @Override
    public void updateSeatInfo(String plainTextKey, Seat seat) {
        long seatId = seat.getId();

        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        String field = String.valueOf(seatId);

        hashOps.put(key, field, seat);

        log.info("Seat field 수정 key: {}, seatId: {}, Seat: {}", key, field, seat);
    }

    @Override
    public void deleteSeatInfo(String plainTextKey, long seatId) {
        String key = RedisKey.SEAT_KEY.getKey() + plainTextKey;
        String field = String.valueOf(seatId);

        hashOps.delete(key, field);
    }
}
