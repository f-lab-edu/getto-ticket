package com.flab.gettoticket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Users {
    private String email;
    private String name;
    private String password;
    private String createdAt;
}
