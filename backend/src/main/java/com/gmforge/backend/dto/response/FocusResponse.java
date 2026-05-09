package com.gmforge.backend.dto.response;

import lombok.Data;

@Data
public class FocusResponse {
    private Long id;
    private int level;
    private String text;
}