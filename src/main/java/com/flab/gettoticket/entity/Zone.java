package com.flab.gettoticket.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Zone {
    private long id;
    private String grade;
    private String name;
    private int price;

    public Zone(long id, String grade, String name, int price) {
        this.id = id;
        this.grade = grade;
        this.name = name;
        this.price = price;
    }
}
