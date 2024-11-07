package com.flab.gettoticket.config;

import com.flab.gettoticket.interceptor.SigninInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SigninInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/user/**", "/register");
    }

}
