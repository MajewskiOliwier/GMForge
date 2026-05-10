package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.CreateAttributeRequest;
import com.gmforge.backend.dto.request.character.UpdateAttributeRequest;
import com.gmforge.backend.dto.response.CharacterAttributeResponse;
import com.gmforge.backend.service.AttributeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attribute")
public class AttributeController {

    private final AttributeService attributeService;

    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @PostMapping("/{characterId}")
    public ResponseEntity<CharacterAttributeResponse> addAttribute(@PathVariable Long characterId,
                                                                   @RequestBody CreateAttributeRequest request) {
        return ResponseEntity.ok(attributeService.addAttribute(characterId, request));
    }

    @GetMapping("/{characterId}")
    public ResponseEntity<List<CharacterAttributeResponse>> getAttributes(@PathVariable Long characterId) {
        return ResponseEntity.ok(attributeService.getAttributes(characterId));
    }

    @GetMapping("/{characterId}/skills")
    public ResponseEntity<List<CharacterAttributeResponse>> getSkills(@PathVariable Long characterId) {
        return ResponseEntity.ok(attributeService.getSkills(characterId));
    }

    @GetMapping("/{characterId}/styles")
    public ResponseEntity<List<CharacterAttributeResponse>> getStyles(@PathVariable Long characterId) {
        return ResponseEntity.ok(attributeService.getStyles(characterId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CharacterAttributeResponse> updateAttribute(@PathVariable Long id,
                                                                      @RequestBody UpdateAttributeRequest request) {
        return ResponseEntity.ok(attributeService.updateAttribute(id, request));
    }
}