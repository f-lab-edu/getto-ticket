package com.flab.gettoticket.interceptor;

import com.flab.gettoticket.model.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class SigninInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("/");
            return false;
        }

        Users user = (Users) session.getAttribute("loginUser");

        if (user == null) {
            response.sendRedirect("/");
            return false;
        }

        return true;
    }

}
