package com.gmforge.backend.repository;

import com.gmforge.backend.entity.User;
import com.gmforge.backend.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    UserPreference findByUser(User user);
}