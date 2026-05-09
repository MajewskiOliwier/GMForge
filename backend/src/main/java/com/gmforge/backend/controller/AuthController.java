package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.LoginRequest;
import com.gmforge.backend.dto.request.RegisterRequest;
import com.gmforge.backend.dto.response.AuthResponse;
import com.gmforge.backend.dto.response.UserSummaryResponse;
import com.gmforge.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}