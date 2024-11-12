package com.flab.gettoticket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Goods {
    private String id;
    private String genreName;
    private String title;
    private String desc;
    private String performanceStartDate;
    private String performanceEndDate;
    private String performanceTime;
    private String location;
    private String x;
    private String y;
    private String genreId;
    private String placeId;
}
