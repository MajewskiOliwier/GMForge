package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.UpdateThemeRequest;
import com.gmforge.backend.dto.response.UserPreferenceResponse;
import com.gmforge.backend.dto.response.UserResponse;
import com.gmforge.backend.dto.response.UserSummaryResponse;
import com.gmforge.backend.service.UserPreferenceService;
import com.gmforge.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserPreferenceService userPreferenceService;

    public UserController(UserService userService,
                          UserPreferenceService userPreferenceService) {
        this.userService = userService;
        this.userPreferenceService = userPreferenceService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUserResponse());
    }

    @GetMapping("/me/preferences")
    public ResponseEntity<UserPreferenceResponse> getPreferences() {
        return ResponseEntity.ok(userPreferenceService.getPreference());
    }

    @PutMapping("/me/preferences/theme")
    public ResponseEntity<UserPreferenceResponse> updateTheme(
            @RequestBody UpdateThemeRequest request) {
        return ResponseEntity.ok(userPreferenceService.updateTheme(request));
    }
}