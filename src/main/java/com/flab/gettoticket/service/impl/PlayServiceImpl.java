package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.dto.PlayTimeDTO;
import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.entity.PlayTime;
import com.flab.gettoticket.repository.PlayRepository;
import com.flab.gettoticket.service.PlayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        if(Objects.isNull(list)) {
            log.error("공연일 리스트 조회 중 예외 발생 goodsId: {}, startDate: {}, endDate: {}", goodsId, startDate, endDate);
            throw new RuntimeException("공연일 리스트 조회에 실패했습니다.");
        }

        return list;
    }

    @Override
    public List<PlayTime> findPlayOrder(long goodsId, LocalDate playAt) {
        List<PlayTime> list = playRepository.selectTimeTableList(goodsId, playAt);

        if(Objects.isNull(list)) {
            log.error("공연 회차 순서 리스트 조회 중 예외 발생 goodsId: {}, playAt: {}", goodsId, playAt);
            throw new RuntimeException("공연 회차 순서 리스트 조회에 실패했습니다.");
        }

        return list;
    }

    @Override
    public PlayTimeDTO findPlayTimeDTO(long playTimeId, long goodsId, LocalDate playAt) {
        PlayTime playTime = playRepository.selectTimeTable(playTimeId, goodsId, playAt);
        List<SeatCountDTO> seatCountDTOList = new ArrayList<>();
        List<String> actorList = new ArrayList<>();

        if(Objects.isNull(playTime)) {
            log.error("회차 정보 조회 중 예외 발생 goodsId: {}, playAt: {}", goodsId, playAt);
            throw new RuntimeException("회차 정보 조회에 실패했습니다.");
        }

        seatCountDTOList = findSeatCount(playTimeId);

        return new PlayTimeDTO(playTime, seatCountDTOList, actorList);
    }

    @Override
    public List<SeatCountDTO> findSeatCount(long playTimeId) {
        List<SeatCountDTO> list = playRepository.selectSeatCount(playTimeId);

        if(Objects.isNull(list)) {
            log.error("회차별 구역 잔여 좌석 개수 playTimeId: {}", playTimeId);
            throw new RuntimeException("회차별 구역 잔여 좌석 개수 조회에 실패했습니다.");
        }

        return list;
    }
}