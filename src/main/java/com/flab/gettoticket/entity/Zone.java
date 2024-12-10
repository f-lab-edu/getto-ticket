package com.flab.gettoticket.entity;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Zone {
    private long id;
    private String grade;
    private String name;
    private int price;
}
