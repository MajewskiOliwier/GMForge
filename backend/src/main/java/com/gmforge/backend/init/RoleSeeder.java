package com.gmforge.backend.init;

import com.gmforge.backend.entity.Role;
import com.gmforge.backend.enums.RoleType;
import com.gmforge.backend.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void seedRoles() {
        for (RoleType type : RoleType.values()) {
            roleRepository.findByName(type)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(type);
                        return roleRepository.save(role);
                    });
        }
    }
}
