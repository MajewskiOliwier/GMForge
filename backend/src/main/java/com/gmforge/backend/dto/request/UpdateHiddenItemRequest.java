package com.gmforge.backend.dto.request;

import lombok.Data;

@Data
public class UpdateHiddenItemRequest {
    private Boolean isHidden;
    private String hiddenName;
    private String hiddenDescription;
}