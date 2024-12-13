package com.flab.gettoticket.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisEntry {
    String key;
    Object value;
}
