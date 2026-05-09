package com.gmforge.backend.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class CharacterResponse {
    private Long id;
    private String name;
    private boolean isNpc;
    private String playerUsername; // null if NPC
    private List<CharacterAttributeResponse> attributes;
    private List<FocusResponse> focuses;
    private List<TruthResponse> truths;
    private List<CharacterItemResponse> items;
}