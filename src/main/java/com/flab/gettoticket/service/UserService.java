package com.flab.gettoticket.service;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.dto.SigninRequest;
import com.flab.gettoticket.dto.SignupReqeust;
import com.flab.gettoticket.entity.Users;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    ApiResponse signUp(SignupReqeust signupReqeust);
    ApiResponse signIn(SigninRequest signinRequest, HttpServletRequest request);
    ApiResponse logout(HttpServletRequest request);
    Users checkUserEmail(String email);
}
