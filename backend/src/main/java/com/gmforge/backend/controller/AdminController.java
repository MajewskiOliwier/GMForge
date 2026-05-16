package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.UpdateThemeRequest;
import com.gmforge.backend.dto.response.UserDetailedResponse;
import com.gmforge.backend.dto.response.UserPreferenceResponse;
import com.gmforge.backend.dto.response.UserResponse;
import com.gmforge.backend.dto.response.UserSummaryResponse;
import com.gmforge.backend.service.AdminService;
import com.gmforge.backend.service.UserPreferenceService;
import com.gmforge.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/promote/{userId}")
    public ResponseEntity<UserDetailedResponse> promoteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.promoteUser(userId));
    }

    @PutMapping("/demote/{userId}")
    public ResponseEntity<UserDetailedResponse> demoteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.demoteUser(userId));
    }
}