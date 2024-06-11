package com.example.comprehensivelogging.common;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
class LoginAspect {
    private static final Logger log = LoggerFactory.getLogger(LoginAspect.class);

    @Before("execution(public DTO.LoginResponse com.example.comprehensivelogging.auth.AuthenticationService.login(..) )")
    public void logUserLoginEvent(JoinPoint joinPoint){
        var methodArgs = joinPoint.getArgs();
        var loginRequest = (DTO.LoginRequest)methodArgs[0];
        var servletRequest = (HttpServletRequest)methodArgs[1];
        log.info("Login attempted by user: {} from IP address: {}", loginRequest.username(), servletRequest.getRemoteAddr());
    }
}
