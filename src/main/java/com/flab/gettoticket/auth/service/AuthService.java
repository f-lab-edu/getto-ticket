package com.flab.gettoticket.auth.service;

import com.flab.gettoticket.auth.dto.UserRequest;
import com.flab.gettoticket.auth.dto.UserResponse;
import com.flab.gettoticket.auth.mapper.UserMapper;
import com.flab.gettoticket.config.jwt.JwtToken;
import com.flab.gettoticket.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Integer signUp(UserRequest userRequest) throws Exception {
        String email = userRequest.getEmail();
        UserResponse userResponse = userMapper.selectUserInfo(email);

        if(userResponse != null) {
            if(userResponse.getEmail() != null) {
                throw new Exception("이미 가입되어 있는 이메일입니다");
            }
        }

        //비밀번호 해시 처리
        String password = userRequest.getPassword();
        userRequest.setPassword(encoder.encode(password));

        return userMapper.insertUser(userRequest);
    }

    public JwtToken signIn(UserRequest userRequest) throws Exception {
        String email = userRequest.getEmail();
        String password = userRequest.getPassword();

        UserResponse userResponse = userMapper.selectUserInfo(email);

        if(userResponse == null) {
            throw new Exception("이메일이 존재하지 않습니다.");
        }
        else {
            if(!encoder.matches(password, userResponse.getPassword())) {
                throw new Exception("비밀번호가 일치하지 않습니다.");
            }
        }

        return jwtTokenProvider.createAccessToken(userResponse);
    }

    public void logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUserEmail(accessToken);

    }
}
