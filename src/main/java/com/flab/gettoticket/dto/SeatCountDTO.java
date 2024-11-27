package com.flab.gettoticket.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class SeatCountDTO {
    private String grade;
    private String zoneName;
    private int count;

    public SeatCountDTO(String grade, String zoneName, int count) {
        this.grade = grade;
        this.zoneName = zoneName;
        this.count = count;
    }
}
