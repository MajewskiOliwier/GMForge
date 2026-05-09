package com.gmforge.backend.dto.response;

import lombok.Data;

@Data
public class PartyResponse {
    private Long id;
    private String name;
    private UserResponse gameMaster;
    private int memberCount;
    private SessionResponse currentSession;
}