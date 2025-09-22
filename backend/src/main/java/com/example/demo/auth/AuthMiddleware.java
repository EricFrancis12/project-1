package com.example.demo.auth;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.exception.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Aspect
@Component
public class AuthMiddleware {

    @Pointcut("@annotation(com.example.demo.auth.Auth)")
    public void authPointcut() {
    }

    @Before("authPointcut()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (requestAttributes == null) {
            throw new RuntimeException("Unable to access request context");
        }

        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        if (response == null) {
            throw new RuntimeException("Unable to access response");
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            throw new UnauthorizedException("Missing Authorization header");
        }

        String[] parts = authHeader.trim().split("\\s+");
        if (parts.length != 2
                || !parts[0].equals("Bearer")
                || !parts[1].equals("TODO_CREATE_AUTH_TOKEN")) {
            throw new UnauthorizedException("Invalid Authorization header");
        }
    }

}
