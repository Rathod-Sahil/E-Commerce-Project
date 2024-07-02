package com.ecommerce.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ecommerce.interceptors.JwtAuthenticationInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor ;

    
    public InterceptorConfig(JwtAuthenticationInterceptor jwtAuthenticationInterceptor) {
        this.jwtAuthenticationInterceptor = jwtAuthenticationInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .excludePathPatterns("/user/login", "/v3/api-docs/**", "/swagger-ui/**","/user/register");
    }
}
