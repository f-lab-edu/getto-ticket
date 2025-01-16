package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.dto.PlayOrderListResponse;
import com.flab.gettoticket.dto.PlayTimeResponse;
import com.flab.gettoticket.dto.SeatCountResponse;
import com.flab.gettoticket.entity.PlayTime;
import com.flab.gettoticket.repository.PlayRepository;
import com.flab.gettoticket.service.PlayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PlayServiceImpl implements PlayService {
    private final PlayRepository playRepository;

    public PlayServiceImpl(PlayRepository playRepository) {
        this.playRepository = playRepository;
    }

    @Override
    public List<String> findPlayAtList(long goodsId, LocalDate startDate, LocalDate endDate) {
        List<String> list = playRepository.selectPlayAtList(goodsId, startDate, endDate);

        if(list.isEmpty()) {
            log.error("공연일 리스트 조회 중 예외 발생 goodsId: {}, startDate: {}, endDate: {}", goodsId, startDate, endDate);
            throw new RuntimeException("공연일 리스트 조회에 실패했습니다.");
        }

        return list;
    }

    @Override
    public List<PlayOrderListResponse> findPlayOrder(long goodsId, LocalDate playAt) {
        List<PlayTime> data = playRepository.selectTimeTableList(goodsId, playAt);
        List<PlayOrderListResponse> list = new ArrayList<>();

        if(data.isEmpty()) {
            log.error("공연 회차 순서 리스트 조회 중 예외 발생 goodsId: {}, playAt: {}", goodsId, playAt);
            throw new RuntimeException("공연 회차 순서 리스트 조회에 실패했습니다.");
        }

        for(PlayTime playTime : data) {
            PlayOrderListResponse response = PlayOrderListResponse.builder()
                                        .playAt(playTime.getPlayAt())
                                        .playOrder(playTime.getPlayOrder())
                                        .playTime(playTime.getPlayTime())
                                        .playTimeId(playTime.getPlayTimeId())
                                        .build();

            list.add(response);
        }

        return list;
    }

    @Override
    public PlayTimeResponse findPlayTimeDTO(long playTimeId, long goodsId) {
        PlayTime playTime = playRepository.selectTimeTable(playTimeId, goodsId);
        List<SeatCountResponse> seatCountDTOList = findSeatCount(playTimeId);
        List<String> actorList = new ArrayList<>();

        return new PlayTimeResponse(playTime, seatCountDTOList, actorList);
    }

    @Override
    public List<SeatCountResponse> findSeatCount(long playTimeId) {
        List<SeatCountResponse> list = playRepository.selectSeatCount(playTimeId);

        if(list.isEmpty()) {
            log.error("회차별 구역 잔여 좌석 개수 playTimeId: {}", playTimeId);
            throw new RuntimeException("회차별 구역 잔여 좌석 개수 조회에 실패했습니다.");
        }

        return list;
    }
}