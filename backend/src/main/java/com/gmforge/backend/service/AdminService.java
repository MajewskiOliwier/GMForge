package com.gmforge.backend.service;

import com.gmforge.backend.dto.response.UserDetailedResponse;
import com.gmforge.backend.dto.response.UserResponse;
import com.gmforge.backend.dto.response.UserSummaryResponse;
import com.gmforge.backend.entity.Role;
import com.gmforge.backend.entity.User;
import com.gmforge.backend.enums.RoleType;
import com.gmforge.backend.repository.RoleRepository;
import com.gmforge.backend.repository.UserRepository;
import com.gmforge.backend.util.PartyAuthHelper;
import com.gmforge.backend.util.RoleHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PartyAuthHelper partyAuthHelper;
    private final RoleRepository roleRepository;
    private final RoleHelper roleHelper;

    public AdminService(UserRepository userRepository, PartyAuthHelper partyAuthHelper, RoleRepository roleRepository, RoleHelper roleHelper) {
        this.userRepository = userRepository;
        this.partyAuthHelper = partyAuthHelper;
        this.roleRepository = roleRepository;
        this.roleHelper = roleHelper;
    }

    public List<UserSummaryResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> !u.getRole().toString().equals(RoleType.ADMIN.toString()))
                .map(user -> new UserSummaryResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail()
                ))
                .toList();
    }

    public UserDetailedResponse promoteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleHelper.getRole(RoleType.MODERATOR.toString());

        user.setRole(role);

        return new UserDetailedResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().getName());
    }


    public UserDetailedResponse demoteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleHelper.getRole(RoleType.USER.toString());

        user.setRole(role);

        return new UserDetailedResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().getName());
    }
}