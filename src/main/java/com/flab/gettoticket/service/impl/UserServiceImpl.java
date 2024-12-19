package com.flab.gettoticket.service.impl;

import com.flab.gettoticket.common.ApiResponse;
import com.flab.gettoticket.dto.SigninRequest;
import com.flab.gettoticket.dto.SignupReqeust;
import com.flab.gettoticket.entity.Users;
import com.flab.gettoticket.repository.UserRepository;
import com.flab.gettoticket.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse signUp(SignupReqeust signupReqeust) {
        String email = signupReqeust.getEmail();
        Users user = checkUserEmail(email);

        if(user != null) {
            return ApiResponse.create(0, "이미 사용중인 이메일입니다.", user);
        }

        String password = signupReqeust.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        signupReqeust.setPassword(encodePassword);

        userRepository.saveUser(signupReqeust);
        return ApiResponse.ok(user);
    }

    @Override
    public ApiResponse signIn(SigninRequest signinRequest, HttpServletRequest request) {
        String email = signinRequest.getEmail();
        String password = signinRequest.getPassword();
        Users user = checkUserEmail(email);

        if(user == null) {
            return ApiResponse.create(0, "아이디 또는 비밀번호가 잘못되었습니다.", null);
        }

        boolean check = passwordEncoder.matches(password, user.getPassword());
        user.setPassword("");

        if(check) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);
            session.setMaxInactiveInterval(1800);    //session 30분 유지
        }

        return ApiResponse.ok(user);
    }

    @Override
    public ApiResponse logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session != null) {
            Users user = (Users) session.getAttribute("loginUser");

            if(user == null) {
                return ApiResponse.create(0, "로그인된 사용자가 아닙니다.", null);
            }

            session.invalidate();
            return ApiResponse.create(1, "로그아웃 처리되었습니다.", null);
        }

        return ApiResponse.create(0, "로그아웃 할 세션이 존재하지 않습니다.", null);
    }

    @Override
    public Users checkUserEmail(String email) {
        return userRepository.findUserEmail(email).orElse(null);
    }

}
