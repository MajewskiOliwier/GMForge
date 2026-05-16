package com.gmforge.backend.repository;

import com.gmforge.backend.entity.Role;
import com.gmforge.backend.entity.User;
import com.gmforge.backend.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}