package com.gmforge.backend.dto.request;

import lombok.Data;

@Data
public class CreateTruthRequest {
    private String name;
    private Long characterId;
}