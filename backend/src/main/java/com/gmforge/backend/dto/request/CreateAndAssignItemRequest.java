package com.gmforge.backend.dto.request;

import com.gmforge.backend.enums.ItemType;
import lombok.Data;

@Data
public class CreateAndAssignItemRequest {
    private String name;
    private String description;
    private ItemType type;
    private Long characterId;
    private Boolean isHidden = false;
    private String hiddenName;
    private String hiddenDescription;
}