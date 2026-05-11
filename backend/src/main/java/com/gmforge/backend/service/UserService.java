package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.CreateSessionRequest;
import com.gmforge.backend.dto.response.UserResponse;
import com.gmforge.backend.dto.response.UserSummaryResponse;
import com.gmforge.backend.entity.User;
import com.gmforge.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username);
    }

    public UserResponse getCurrentUserResponse() {
        User user = getCurrentUser();
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;
    }

    public List<UserSummaryResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserSummaryResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail()
                ))
                .toList();
    }

    public UserResponse getByUserName(String userName) {
        User user = userRepository.findByUsername(userName);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());

        return  userResponse;
    }
}