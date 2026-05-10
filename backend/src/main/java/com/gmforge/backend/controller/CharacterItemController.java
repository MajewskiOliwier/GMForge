package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.CreateAndAssignItemRequest;
import com.gmforge.backend.dto.request.UpdateCharacterItemRequest;
import com.gmforge.backend.dto.request.UpdateHiddenItemRequest;
import com.gmforge.backend.dto.response.CharacterItemResponse;
import com.gmforge.backend.service.CharacterItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/character-item")
public class CharacterItemController {

    private final CharacterItemService characterItemService;

    public CharacterItemController(CharacterItemService characterItemService) {
        this.characterItemService = characterItemService;
    }

    @PostMapping("/{characterId}")
    public ResponseEntity<CharacterItemResponse> addItem(@PathVariable Long characterId,
                                                         @RequestBody CreateAndAssignItemRequest request) {
        return ResponseEntity.ok(characterItemService.addItem(characterId, request));
    }

    @GetMapping("/{characterId}")
    public ResponseEntity<List<CharacterItemResponse>> getItems(@PathVariable Long characterId) {
        return ResponseEntity.ok(characterItemService.getItems(characterId));
    }

    @PutMapping("/{id}/hidden")
    public ResponseEntity<CharacterItemResponse> updateHidden(@PathVariable Long id,
                                                              @RequestBody UpdateHiddenItemRequest request) {
        return ResponseEntity.ok(characterItemService.updateHidden(id, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CharacterItemResponse> updateItem(@PathVariable Long id,
                                                            @RequestBody UpdateCharacterItemRequest request) {
        return ResponseEntity.ok(characterItemService.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeItem(@PathVariable Long id) {
        characterItemService.removeItem(id);
        return ResponseEntity.noContent().build();
    }
}