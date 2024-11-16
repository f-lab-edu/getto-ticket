package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.dto.SeatDTO;
import com.flab.gettoticket.dto.SeatCountDTO;
import com.flab.gettoticket.model.PlayTime;
import com.flab.gettoticket.repository.PlayRepository;
import com.flab.gettoticket.service.PlayService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayServiceImpl implements PlayService {
    private final PlayRepository playRepository;

    public PlayServiceImpl(PlayRepository playRepository) {
        this.playRepository = playRepository;
    }

    @Override
    public List<String> findPlayAtList(String goodsId, String startDate, String endDate) {
        return playRepository.selectPlayAtList(goodsId, startDate, endDate);
    }

    @Override
    public List<PlayTime> findPlayOrder(String goodsId, String playAt) {
        return playRepository.selectTimeTable(goodsId, playAt);
    }

    @Override
    public List<SeatCountDTO> findSeatCount(String playTimeId) {
        return playRepository.selectSeatCount(playTimeId);
    }

    @Override
    public SeatDTO findSeatDTO(String goodsId, String playAt) {
        SeatDTO seatDateDto = new SeatDTO();
        List<PlayTime> timeTableList = new ArrayList<>();
        List<SeatCountDTO> seatCountDTOList = new ArrayList<>();

        try {
            timeTableList = findPlayOrder(goodsId, playAt);

            if(!timeTableList.isEmpty()) {
                String playTimeId = timeTableList.get(0).getPlayTimeId();
                seatCountDTOList = findSeatCount(playTimeId);
            }

            seatDateDto.setTimeTableList(timeTableList);
            seatDateDto.setSeatCountList(seatCountDTOList);
        } catch(Exception e) {
            e.printStackTrace();;
        }

        return seatDateDto;
    }
}
