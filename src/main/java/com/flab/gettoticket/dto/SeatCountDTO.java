package com.flab.gettoticket.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SeatCountDTO {
    private String grade;
    private String zoneName;
    private int count;
}
