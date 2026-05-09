package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.UpdateThemeRequest;
import com.gmforge.backend.dto.response.UserPreferenceResponse;
import com.gmforge.backend.entity.User;
import com.gmforge.backend.entity.UserPreference;
import com.gmforge.backend.repository.UserPreferenceRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserService userService;

    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository,
                                 UserService userService) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userService = userService;
    }

    public UserPreferenceResponse getPreference() {
        User user = userService.getCurrentUser();
        UserPreference preference = userPreferenceRepository.findByUser(user);
        return mapToResponse(preference);
    }

    public UserPreferenceResponse updateTheme(UpdateThemeRequest request) {
        User user = userService.getCurrentUser();
        UserPreference preference = userPreferenceRepository.findByUser(user);
        preference.setTheme(request.getTheme());
        userPreferenceRepository.save(preference);
        return mapToResponse(preference);
    }

    private UserPreferenceResponse mapToResponse(UserPreference preference) {
        UserPreferenceResponse response = new UserPreferenceResponse();
        response.setId(preference.getId());
        response.setTheme(preference.getTheme());
        return response;
    }
}