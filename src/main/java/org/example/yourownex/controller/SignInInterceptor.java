package org.example.yourownex.controller;

import jakarta.servlet.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.servlet.*;

@Component
public class SignInInterceptor implements HandlerInterceptor {
    private static final ThreadLocal<Long> holder = new ThreadLocal<>();

    public static Long getUserId() {
        return holder.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        holder.set(userId);
        return userId != null;
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
