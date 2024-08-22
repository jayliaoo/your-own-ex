package org.example.yourownex.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.yourownex.service.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SignInInterceptor implements HandlerInterceptor {
    private final JWTService jwtService;
    private static final ThreadLocal<Long> holder = new ThreadLocal<>();

    public SignInInterceptor(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    public static Long getUserId() {
        return holder.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        if (header == null || header.length() < 7) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        try {
            Long userId = jwtService.verify(header.substring(7));
            holder.set(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) {
        holder.remove();
    }
}
