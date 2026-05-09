package com.gmforge.backend.dto.response;

import lombok.Data;

@Data
public class PartyCardResponse {
    private Long id;
    private String name;
    private String gameMasterUsername;
    private int memberCount;
    private String currentSessionName; // itis optional as the sessions dont require names
    private int currentSessionNumber;
}