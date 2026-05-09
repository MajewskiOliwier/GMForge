package com.gmforge.backend.dto.request;

import lombok.Data;

@Data
public class AssignItemRequest {
    private Long itemId;
    private Long characterId;
    private Long fromCharacterId; // will be null if the transfer wasnt between characters
    private Boolean isHidden = false;
    private String hiddenName;
    private String hiddenDescription;
}