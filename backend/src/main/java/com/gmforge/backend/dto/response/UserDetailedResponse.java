package com.gmforge.backend.dto.response;

import com.gmforge.backend.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailedResponse {
    private Long id;
    private String username;
    private String email;
    private RoleType role;
}