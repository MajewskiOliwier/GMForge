package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.CreateFocusRequest;
import com.gmforge.backend.dto.request.character.UpdateFocusRequest;
import com.gmforge.backend.dto.response.FocusResponse;
import com.gmforge.backend.service.FocusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/focus")
public class FocusController {

    private final FocusService focusService;

    public FocusController(FocusService focusService) {
        this.focusService = focusService;
    }

    @PostMapping("/{characterId}")
    public ResponseEntity<FocusResponse> addFocus(@PathVariable Long characterId,
                                                  @RequestBody CreateFocusRequest request) {
        return ResponseEntity.ok(focusService.addFocus(characterId, request));
    }

    @GetMapping("/{characterId}")
    public ResponseEntity<List<FocusResponse>> getFocuses(@PathVariable Long characterId) {
        return ResponseEntity.ok(focusService.getFocuses(characterId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FocusResponse> updateFocus(@PathVariable Long id,
                                                     @RequestBody UpdateFocusRequest request) {
        return ResponseEntity.ok(focusService.updateFocus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFocus(@PathVariable Long id) {
        focusService.deleteFocus(id);
        return ResponseEntity.noContent().build();
    }
}