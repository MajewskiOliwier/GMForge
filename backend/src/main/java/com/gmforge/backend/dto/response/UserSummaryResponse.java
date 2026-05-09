package com.gmforge.backend.dto.response;

public record UserSummaryResponse(
        Long id,
        String username,
        String email
) {}