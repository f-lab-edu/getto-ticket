package com.flab.gettoticket.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private String placeName;
}
