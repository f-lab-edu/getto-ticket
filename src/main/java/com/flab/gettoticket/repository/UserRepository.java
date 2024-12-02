package com.flab.gettoticket.repository;

import com.flab.gettoticket.dto.SignupReqeust;
import com.flab.gettoticket.entity.Users;

import java.util.Optional;

public interface UserRepository {
    int saveUser(SignupReqeust signupReqeust);
    Optional<Users> findUserEmail(String email);

}
