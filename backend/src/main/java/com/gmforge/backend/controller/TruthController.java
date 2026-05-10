package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.CreateTruthRequest;
import com.gmforge.backend.dto.request.UpdateTruthRequest;
import com.gmforge.backend.dto.response.TruthResponse;
import com.gmforge.backend.service.TruthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/truth")
public class TruthController {

    private final TruthService truthService;

    public TruthController(TruthService truthService) {
        this.truthService = truthService;
    }

    @PostMapping("/{characterId}")
    public ResponseEntity<TruthResponse> addTruth(@PathVariable Long characterId,
                                                  @RequestBody CreateTruthRequest request) {
        return ResponseEntity.ok(truthService.addTruth(characterId, request));
    }

    @GetMapping("/{characterId}")
    public ResponseEntity<List<TruthResponse>> getTruths(@PathVariable Long characterId) {
        return ResponseEntity.ok(truthService.getTruths(characterId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TruthResponse> updateTruth(@PathVariable Long id,
                                                     @RequestBody UpdateTruthRequest request) {
        return ResponseEntity.ok(truthService.updateTruth(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTruth(@PathVariable Long id) {
        truthService.deleteTruth(id);
        return ResponseEntity.noContent().build();
    }
}