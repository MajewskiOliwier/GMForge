package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.CreateCharacterRequest;
import com.gmforge.backend.dto.request.character.UpdateCharacterNameRequest;
import com.gmforge.backend.dto.response.CharacterResponse;
import com.gmforge.backend.service.CharacterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/character")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping("/{partyId}")
    public ResponseEntity<CharacterResponse> createCharacter(@PathVariable Long partyId,
                                                             @RequestBody CreateCharacterRequest request) {
        return ResponseEntity.ok(characterService.createCharacter(partyId, request));
    }

    @GetMapping("/{partyId}")
    public ResponseEntity<List<CharacterResponse>> getCharacters(@PathVariable Long partyId) {
        return ResponseEntity.ok(characterService.getCharacters(partyId));
    }

    @GetMapping("/{partyId}/npcs")
    public ResponseEntity<List<CharacterResponse>> getNpcs(@PathVariable Long partyId) {
        return ResponseEntity.ok(characterService.getNpcs(partyId));
    }

    @GetMapping("/{partyId}/players")
    public ResponseEntity<List<CharacterResponse>> getPlayers(@PathVariable Long partyId) {
        return ResponseEntity.ok(characterService.getPlayers(partyId));
    }

    @GetMapping("/single/{id}")
    public ResponseEntity<CharacterResponse> getCharacterById(@PathVariable Long id) {
        return ResponseEntity.ok(characterService.getCharacterById(id));
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<CharacterResponse> updateCharacterName(@PathVariable Long id,
                                                                 @RequestBody UpdateCharacterNameRequest request) {
        return ResponseEntity.ok(characterService.updateCharacterName(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}