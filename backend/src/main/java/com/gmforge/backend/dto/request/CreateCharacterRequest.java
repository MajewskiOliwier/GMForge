package com.gmforge.backend.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateCharacterRequest {
    private String name;
//    private Long partyId;
    private Long userId; // if null then it is NPC
    private List<CreateAttributeRequest> attributes;
    private List<CreateFocusRequest> focuses;
    private List<CreateAndAssignItemRequest> items;
}