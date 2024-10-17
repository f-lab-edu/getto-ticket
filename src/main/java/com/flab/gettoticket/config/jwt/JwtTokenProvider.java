package com.flab.gettoticket.config.jwt;

import com.flab.gettoticket.auth.dto.UserResponse;
import com.flab.gettoticket.auth.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;

    // application.yml에서 secret 값 가져와서 key에 저장
    public JwtTokenProvider(
        @Value("${jwt.secret}") String secretKey
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Access Token 생성
     * @param userResponse
     * @return Access Token String
     */
    public JwtToken createAccessToken(UserResponse userResponse) {
        return generateToken(userResponse);
    }

    public JwtToken generateToken(UserResponse userResponse) {
        Claims claims = Jwts.claims();
        claims.put("userEmail", userResponse.getEmail());
        claims.put("userName", userResponse.getName());
        claims.put("userPassword", userResponse.getPassword());
        claims.put("userRole", userResponse.getRole());

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 86400000);
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken, CustomUserDetailsService customUserDetailsService) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("userRole") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // UserDetails 객체를 만들어서 Authentication return
        String email = getUserEmail(accessToken);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    /**
     * Request Header에서 토큰 정보 추출
     * @param request
     * @return
     */
    public String resolveToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        // jwt 헤더가 있는 경우
        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer")) {
            return accessToken.substring(7);
        }
        return null;
    }

    /**
     * Token에서 User Email 추출
     * @param token
     * @return User Email
     */
    public String getUserEmail(String token) {
        return parseClaims(token).get("userEmail", String.class);
    }

    /**
     * 남은 에세스토큰의 만료시간 조회
     * @param accessToken
     * @return
     */
    public Long getExpiration(String accessToken){
        // 토큰 만료시간
        Date expiration = parseClaims(accessToken).getExpiration();

        // 현재시간
        long now = new Date().getTime();
        return (expiration.getTime()-now);
    }

    // accessToken
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
