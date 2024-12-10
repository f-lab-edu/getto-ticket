package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.entity.Zone;
import com.flab.gettoticket.repository.SeatRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {
    @InjectMocks
    private SeatServiceImpl seastServiceImpl;

    @Mock
    private SeatRepository seatRepository;

    @Test
    @DisplayName("좌석 가격 조회")
    void findZonePrice(){
        //given
        long id = 1;

        when(seatRepository.selectZonePrice(id))
                .thenReturn(createZoneList());

        //when
        List<Zone> list = seastServiceImpl.findZonePrice(id);

        //then
        Assertions.assertThat(list)
                .hasSize(2)
                .extracting(Zone::getId, Zone::getName)
                .contains(
                        tuple(1L, "VIP")
                        , tuple(2L, "R")
                );
    }

    private List<Zone> createZoneList() {
        return Arrays.asList(
                new Zone(1, "1", "VIP", 170000)
                , new Zone(2, "2", "R", 140000)
        );
    }
}