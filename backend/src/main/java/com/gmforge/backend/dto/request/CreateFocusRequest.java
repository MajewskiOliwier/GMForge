package com.gmforge.backend.dto.request;

import lombok.Data;

@Data
public class CreateFocusRequest {
    private int level;
    private String text;
}