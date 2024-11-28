package com.flab.gettoticket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Zone {
    private long id;
    private String grade;
    private String name;
    private int price;
}
