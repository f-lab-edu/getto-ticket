package com.flab.gettoticket.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
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

    public Goods(long id, String genreName, String title, String desc, LocalDate performanceStartDate, LocalDate performanceEndDate, String performanceTime, String location, String x, String y, long genreId, long placeId) {
        this.id = id;
        this.genreName = genreName;
        this.title = title;
        this.desc = desc;
        this.performanceStartDate = performanceStartDate;
        this.performanceEndDate = performanceEndDate;
        this.performanceTime = performanceTime;
        this.location = location;
        this.x = x;
        this.y = y;
        this.genreId = genreId;
        this.placeId = placeId;
    }
}
