package com.gmforge.backend.dto.response;

import com.gmforge.backend.enums.ItemType;
import lombok.Data;

@Data
public class CharacterItemResponse {
    private Long id;
    private String name;
    private String description;
    private ItemType type;
    private Boolean isHidden;
    private String hiddenName;
    private String hiddenDescription;
}