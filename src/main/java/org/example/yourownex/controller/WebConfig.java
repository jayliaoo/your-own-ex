package org.example.yourownex.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SignInInterceptor signInInterceptor;

    public WebConfig(SignInInterceptor signInInterceptor) {
        this.signInInterceptor = signInInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signInInterceptor)
                .excludePathPatterns("/sign/**")
                .addPathPatterns("/**");

    }
}
