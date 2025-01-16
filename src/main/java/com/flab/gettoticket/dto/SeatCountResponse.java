package com.flab.gettoticket.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SeatCountResponse {
    private String grade;
    private String zoneName;
    private int count;

    public SeatCountResponse(String grade, String zoneName, int count) {
        this.grade = grade;
        this.zoneName = zoneName;
        this.count = count;
    }
}
