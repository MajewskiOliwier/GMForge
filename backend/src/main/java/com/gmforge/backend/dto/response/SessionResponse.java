package com.gmforge.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SessionResponse {
    private Long id;
    private String name;
    private int sessionNumber;
    private LocalDateTime createdAt;
}