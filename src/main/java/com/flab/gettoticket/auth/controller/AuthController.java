package com.flab.gettoticket.auth.controller;

import com.flab.gettoticket.auth.dto.UserRequest;
import com.flab.gettoticket.auth.service.AuthService;
import com.flab.gettoticket.auth.service.CustomUserDetails;
import com.flab.gettoticket.config.jwt.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /** TODO
     * [] exception 전역 처리
     * [] logout() -> Redis로 관리 및 구현
     * [] http response 로 줄 객체 공통화
     */

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody UserRequest userRequest) {
        Map<String, Object> resMap = new HashMap<>();
        Integer resultCode = 0;
        String message = "회원가입에 실패했습니다.";

        try {
            resultCode = authService.signUp(userRequest);
            message = "회원가입이 완료되었습니다.";
        } catch (Exception e) {
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            resMap.put("message", message);
            resMap.put("resultCode", resultCode);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resMap);
    }
    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@RequestBody UserRequest userRequest) {
        Map<String, Object> resMap = new HashMap<>();
        String message = "로그인에 실패했습니다.";
        JwtToken jwtToken = null;

        try {
            jwtToken = authService.signIn(userRequest);
            message = "로그인에 성공했습니다.";
        } catch (Exception e) {
            message = e.getMessage();
        } finally {
            resMap.put("message", message);
            resMap.put("jwtToken", jwtToken);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resMap);
    }

    @DeleteMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request){
        authService.logout(request);
        return null;

    }
}
