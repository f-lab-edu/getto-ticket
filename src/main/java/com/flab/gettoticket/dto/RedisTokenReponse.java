package com.flab.gettoticket.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisTokenReponse {
    private long userSeq;
    private String email;
    private long goodsId;
    private long playTimeId;
    private long rank;
    private String status;
    private String reqDateTime;
}
