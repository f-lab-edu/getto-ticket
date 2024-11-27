package com.flab.gettoticket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Goods {
    private long id;
    private String genreName;
    private String title;
    private String desc;
    private LocalDate performanceStartDate;
    private LocalDate performanceEndDate;
    private String performanceTime;
    private String location;
    private String x;
    private String y;
    private long genreId;
    private long placeId;
}
