package com.example.comprehensivelogging.auth;


import com.example.comprehensivelogging.common.DTO;
import com.example.comprehensivelogging.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Tag(name = "Authentication", description = "API endpoint for authentication related requests")
@RestController
@RequestMapping("/api/v1/auth")
class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    AuthController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Register as a User", description = "Register to use the application")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DTO.RegisterUserResponse> registerUser(
            @Valid @RequestBody DTO.RegisterUserRequest registerUserRequest){
        var regUser = userService.registerUser(authenticationService.encodeRawPassword(registerUserRequest));
        var location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/users/{id}")
                .buildAndExpand(regUser.id()).toUri();
        return ResponseEntity.created(location).body(regUser);
    }

    @Operation(summary = "Login", description = "Login to use the application")
    @PostMapping("/login")
    DTO.LoginResponse login(@Valid @RequestBody DTO.LoginRequest loginRequest,
                            HttpServletRequest request){
        return authenticationService.login(loginRequest, request);
    }
}
