package com.gmforge.backend.dto.request.character;

import lombok.Data;

@Data
public class UpdateFocusRequest {
    private int level;
    private String text;
}
