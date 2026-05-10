package com.gmforge.backend.dto.request;

import com.gmforge.backend.enums.SkillType;
import com.gmforge.backend.enums.StyleType;
import lombok.Data;

@Data
public class CreateAttributeRequest {
    private SkillType skill;
    private StyleType style;
    private int level;
}