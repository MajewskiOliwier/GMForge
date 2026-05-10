package com.gmforge.backend.dto.request;

import lombok.Data;

@Data
public class UpdateCharacterItemRequest {
    private String name;
    private String description;
}