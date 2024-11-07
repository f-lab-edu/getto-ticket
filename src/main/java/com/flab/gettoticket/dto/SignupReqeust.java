package com.flab.gettoticket.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignupReqeust {
    private String email;
    private String name;
    private String password;
    private String createdAt;
}
