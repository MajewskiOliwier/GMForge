package com.gmforge.backend.util;

import com.gmforge.backend.entity.Role;
import com.gmforge.backend.enums.RoleType;
import com.gmforge.backend.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class RoleHelper {
    private final RoleRepository roleRepository;

    public RoleHelper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRole(String role) {
        return roleRepository.findByName(RoleType.MODERATOR)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(RoleType.MODERATOR);
                    return roleRepository.save(newRole);
                });
    }

}
