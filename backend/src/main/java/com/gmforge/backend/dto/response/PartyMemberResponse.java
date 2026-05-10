package com.gmforge.backend.dto.response;

import lombok.Data;

@Data
public class PartyMemberResponse {
    private Long id;
    private String username;
    private String email;
    private boolean isGameMaster;
}