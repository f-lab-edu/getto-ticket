package com.flab.gettoticket.auth.service;

import com.flab.gettoticket.auth.dto.UserResponse;
import com.flab.gettoticket.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserResponse userResponse = userMapper.selectUserInfo(email);

        if(userResponse == null) {
            throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
        }

        // 권한 설정 (SimpleGrantedAuthority를 사용)
        String role = userResponse.getRole().equals("ADMIN") ? "ROLE_ADMIN" : "ROLE_USER";
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        CustomUserDetails detail = CustomUserDetails.builder()
                .userEmail(userResponse.getEmail())
                .userName(userResponse.getName())
                .userPassword(userResponse.getPassword())
                .userRole(userResponse.getRole())
                .role(authorities)
                .build();

        return detail;
    }
}
