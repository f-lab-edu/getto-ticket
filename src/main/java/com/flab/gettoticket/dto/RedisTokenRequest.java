package com.flab.gettoticket.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisTokenRequest {
    private String email;
    private long goodsId;
    private long playTimeId;
    private LocalDate playAt;
    private String token;
}
