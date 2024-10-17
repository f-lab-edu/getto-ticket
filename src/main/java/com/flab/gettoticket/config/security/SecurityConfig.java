package com.flab.gettoticket.config.security;

import com.flab.gettoticket.auth.service.CustomUserDetailsService;
import com.flab.gettoticket.config.jwt.JwtAccessDeniedHandler;
import com.flab.gettoticket.config.jwt.JwtAuthenticationEntryPoint;
import com.flab.gettoticket.config.jwt.JwtAuthenticationFilter;
import com.flab.gettoticket.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private static final String[] ROLE_WHITELIST = {"/home", "/auth/**"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF, CORS
        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults());

        // 세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        // Jwt Exception
        http.exceptionHandling((exceptionHandling) ->
            exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                            .accessDeniedHandler(jwtAccessDeniedHandler)
        );

        // 권한 규칙 작성
        http.authorizeHttpRequests(authorize ->
            authorize.requestMatchers(ROLE_WHITELIST).permitAll()   //AUTH_WHITELIST 요청에 대해서는 모든 요청 허가
//                    .requestMatchers("/test").hasAnyRole("USER")
                    .anyRequest().authenticated()   //AUTH_WHITELIST 이외의 요청은 인증 필요
        );

        return http.build();
    }

}
