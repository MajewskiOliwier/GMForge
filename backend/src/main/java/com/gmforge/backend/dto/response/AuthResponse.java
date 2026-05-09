package com.gmforge.backend.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwtToken;
    private UserResponse user;
}