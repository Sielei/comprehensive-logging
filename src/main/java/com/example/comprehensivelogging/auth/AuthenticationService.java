package com.example.comprehensivelogging.auth;

import com.example.comprehensivelogging.common.DTO;
import com.example.comprehensivelogging.common.exception.UserAuthenticationException;
import com.example.comprehensivelogging.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
class AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(UserService userService, AuthenticationManager authenticationManager,
                                 BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public DTO.LoginResponse login(DTO.LoginRequest loginRequest, HttpServletRequest request) {
        log.info("Login attempted by user: {} from IP address: {}", loginRequest.username(), request.getRemoteAddr());
        var userDto = userService.findUserByUsername(loginRequest.username())
                .orElseThrow(() -> new UserAuthenticationException("Failed to authenticate user! Username or password is incorrect"));
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.username(),
                loginRequest.password());
        var isAuthenticated = false;
        try {
            var authManager = authenticationManager.authenticate(authenticationToken);
            isAuthenticated = authManager.isAuthenticated();
            log.info("{} authenticated successfully", userDto.username());
        }
        catch (AuthenticationException authenticationException){
            log.error("Authentication for {} failed because of {}!", userDto.username(), authenticationException.getMessage());
        }
        if (isAuthenticated){
            var jwtToken = jwtUtil.generateJWTToken(userDto);
            return new DTO.LoginResponse(jwtToken, "Bearer");
        }
        else {
            throw new UserAuthenticationException("Failed to authenticate user! Username or password is incorrect");
        }
    }
    public DTO.RegisterUserRequest encodeRawPassword(DTO.RegisterUserRequest registerUserRequest){
        return new DTO.RegisterUserRequest(registerUserRequest.firstName(), registerUserRequest.lastName(),
                registerUserRequest.email(), bCryptPasswordEncoder.encode(registerUserRequest.password()));
    }
}
