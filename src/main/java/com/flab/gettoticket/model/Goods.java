package com.flab.gettoticket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Goods {
    private long id;
    private String genreName;
    private String title;
    private String desc;
    private String performanceStartDate;
    private String performanceEndDate;
    private String performanceTime;
    private String location;
    private String x;
    private String y;
    private long genreId;
    private long placeId;
}
