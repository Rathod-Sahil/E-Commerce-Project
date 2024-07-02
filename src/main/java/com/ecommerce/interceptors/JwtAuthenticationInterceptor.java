package com.ecommerce.interceptors;

import com.ecommerce.annotations.Access;
import com.ecommerce.decorators.RequestSession;
import com.ecommerce.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ecommerce.exceptions.TokenExpiredException;
import com.ecommerce.exceptions.UnauthorizedException;
import com.ecommerce.utils.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.HashSet;


@RequiredArgsConstructor
@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor{

    private final JwtUtils jwtUtils;
    private final RequestSession requestSession;


    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        Access access = ((HandlerMethod) handler).getMethodAnnotation(Access.class);
        assert access != null;
        if(Arrays.toString(access.role()).contains(Role.ANONYMOUS.toString())){
            return true;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing authorization header");
        }

        jwt = authHeader.substring(7);

        jwtUtils.extractToken(jwt);

        if(jwtUtils.isTokenExpired()) {
            throw new TokenExpiredException();
        }

        HashSet<Role> collect = new HashSet<>(requestSession.getJwtUser().getRole());

        if(collect.contains(Role.ANONYMOUS.toString()) || Arrays.stream(access.role()).anyMatch(o -> collect.contains(o.toString()))){
            return true;
        }

        throw new UnauthorizedException("You don't have rights to access this api");
    }
}
