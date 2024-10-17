package com.flab.gettoticket.config.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class JwtErrorResponse {

    private int status;
    private String message;

}
