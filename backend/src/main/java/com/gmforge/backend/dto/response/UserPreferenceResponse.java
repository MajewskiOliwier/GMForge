package com.gmforge.backend.dto.response;

import com.gmforge.backend.enums.Theme;
import lombok.Data;

@Data
public class UserPreferenceResponse {
    private Long id;
    private Theme theme;
}