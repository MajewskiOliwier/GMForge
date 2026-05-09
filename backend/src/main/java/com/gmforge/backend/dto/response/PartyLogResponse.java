package com.gmforge.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PartyLogResponse {
    private Long id;
    private String description;
    private String createdByUsername;
    private LocalDateTime createdAt;
}