package com.flab.gettoticket.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequest {
    private String email;
    private String name;
    private String password;
    private String role;
}
