package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.dto.SeatDTO;
import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.model.PlayTime;
import com.flab.gettoticket.repository.PlayRepository;
import com.flab.gettoticket.service.PlayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlayServiceImpl implements PlayService {
    private final PlayRepository playRepository;

    public PlayServiceImpl(PlayRepository playRepository) {
        this.playRepository = playRepository;
    }

    @Override
    public List<String> findPlayAtList(long goodsId, LocalDate startDate, LocalDate endDate) {
        List<String> list = new ArrayList<>();
        try {
            list = playRepository.selectPlayAtList(goodsId, startDate, endDate);
        } catch (Exception e) {
            log.error("공연일 리스트 조회 중 예외 발생 goodsId: {}. startDate: {}. endDate: {}. Error: {}", goodsId, startDate, endDate, e.getMessage(), e);
            throw new RuntimeException("공연일 리스트 조회에 실패했습니다.", e);
        }
        return list;
    }

    @Override
    public List<PlayTime> findPlayOrder(long goodsId, LocalDate playAt) {
        List<PlayTime> list = new ArrayList<>();
        try {
            list = playRepository.selectTimeTable(goodsId, playAt);
        } catch (Exception e) {
            log.error("공연 회차 순서 리스트 조회 중 예외 발생 goodsId: {}. playAt: {}. Error: {}", goodsId, playAt, e.getMessage(), e);
            throw new RuntimeException("공연 회차 순서 리스트 조회에 실패했습니다.", e);
        }
        return list;
    }

    @Override
    public List<SeatCountDTO> findSeatCount(long playTimeId) {
        List<SeatCountDTO> list = new ArrayList<>();
        try {
            list = playRepository.selectSeatCount(playTimeId);
        } catch (Exception e) {
            log.error("n회차의 구역별 잔여 좌석 개수 playTimeId: {}, Error: {}", playTimeId, e.getMessage(), e);
            throw new RuntimeException("n회차의 구역별 잔여 좌석 개수 조회에 실패했습니다.", e);
        }
        return list;
    }

    @Override
    public SeatDTO findSeatDTO(long goodsId, LocalDate playAt) {
        List<PlayTime> timeTableList = new ArrayList<>();
        List<SeatCountDTO> seatCountDTOList = new ArrayList<>();

        try {
            timeTableList = findPlayOrder(goodsId, playAt);

            if(!timeTableList.isEmpty()) {
                long playTimeId = timeTableList.get(0).getPlayTimeId();
                seatCountDTOList = findSeatCount(playTimeId);
            }

            return new SeatDTO(timeTableList, seatCountDTOList);
        } catch(Exception e) {
            log.error("첫번째 회차의 구역별 잔여 좌석 개수 조회 중 예외 발생 goodsId: {} and playAt: {}. Error: {}", goodsId, playAt, e.getMessage(), e);
            throw new RuntimeException("구역별 잔여 좌석 개수 조회에 실패했습니다.", e);
        }
    }
}
