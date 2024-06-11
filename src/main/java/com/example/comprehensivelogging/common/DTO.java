package com.example.comprehensivelogging.common;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class DTO {
    public record UserDto(Long id, String username, String password){}
    public record LoginRequest(String username, String password){}
    public record LoginResponse(String token, String type){}
    public record RegisterUserRequest(@NotBlank(message = "First name is required") String firstName,
                                      @NotBlank(message = "Last name is required") String lastName,
                                      @Email(message = "Email should be valid") String email,
                                      @NotBlank(message = "Password is required") String password){}
    public record RegisterUserResponse(Long id, String name, String email){}
}
