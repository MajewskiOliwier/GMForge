package com.gmforge.backend.dto.request;

import lombok.Data;

@Data
public class CreateCharacterRequest {
    private String name;
    private Long partyId;
    private Long userId; // if null then it is NPC
}