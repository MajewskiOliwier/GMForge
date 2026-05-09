package com.gmforge.backend.dto.request;

import lombok.Data;

@Data
public class CreateSessionRequest {
    private String name; // session name is optional
}