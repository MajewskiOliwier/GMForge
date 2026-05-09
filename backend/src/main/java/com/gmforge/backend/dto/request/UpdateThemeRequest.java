package com.gmforge.backend.dto.request;

import com.gmforge.backend.enums.Theme;
import lombok.Data;

@Data
public class UpdateThemeRequest {
    private Theme theme;
}