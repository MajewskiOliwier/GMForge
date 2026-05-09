package com.gmforge.backend.dto.request;

import com.gmforge.backend.enums.ItemType;
import lombok.Data;

@Data
public class CreateItemRequest {
    private String name;
    private String description;
    private ItemType type;
}