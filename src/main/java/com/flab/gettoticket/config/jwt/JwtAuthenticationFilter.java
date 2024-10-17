package com.flab.gettoticket.config.jwt;

import com.flab.gettoticket.auth.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // JWT 토큰 검증 필터 수행
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. Request Header에서 JWT 토큰 추출
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        // 2. validateToken으로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체 추출
            Authentication authentication = jwtTokenProvider.getAuthentication(token, customUserDetailsService);

            // 현재 Request의 SecurityContext에 접근권한 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
//        else {
//            log.info("인증 실패 ==> JwtAuthenticationFilter");
//
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            int httpStatus = HttpStatus.UNAUTHORIZED.value();
//            JwtErrorResponse jwtErrorResponse = JwtErrorResponse.builder()
//                    .status(httpStatus)
//                    .message("인증이 실패했습니다.")
//                    .build();
//
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
//            httpResponse.setStatus(httpStatus);
//            httpResponse.setCharacterEncoding("utf-8");
//            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json
//            httpResponse.getWriter().write(objectMapper.writeValueAsString(jwtErrorResponse));
//        }

        chain.doFilter(request, response);
    }


}
