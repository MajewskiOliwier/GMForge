package com.gmforge.backend.dto.response;

import com.gmforge.backend.enums.SkillType;
import com.gmforge.backend.enums.StyleType;
import lombok.Data;

@Data
public class CharacterAttributeResponse {
    private Long id;
    private SkillType skill;
    private StyleType style;
    private int level;
}