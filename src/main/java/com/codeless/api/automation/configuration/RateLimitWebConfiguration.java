package com.codeless.api.automation.configuration;

import com.codeless.api.automation.configuration.interceptor.RateLimitRequestInterceptor;
import com.codeless.api.automation.configuration.interceptor.AuthRateLimitRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RateLimitWebConfiguration implements WebMvcConfigurer {

    @Autowired RateLimitRequestInterceptor rateLimitRequestInterceptor;
    @Autowired AuthRateLimitRequestInterceptor authRateLimitRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO add appropriate patterns for other endpoints
        registry.addInterceptor(rateLimitRequestInterceptor).addPathPatterns("/tests/**");
        registry.addInterceptor(authRateLimitRequestInterceptor).addPathPatterns("/auth/sign-in", "/auth/sign-out", "/users", "/verify" );
    }
}