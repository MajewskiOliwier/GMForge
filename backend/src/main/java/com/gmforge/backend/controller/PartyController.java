package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.CreatePartyRequest;
import com.gmforge.backend.dto.request.UpdatePartyRequest;
import com.gmforge.backend.dto.response.PartyCardResponse;
import com.gmforge.backend.dto.response.PartyResponse;
import com.gmforge.backend.service.PartyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/party")
public class PartyController {

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @PostMapping
    public ResponseEntity<PartyResponse> createParty(@RequestBody CreatePartyRequest request) {
        return ResponseEntity.ok(partyService.createParty(request));
    }

    @GetMapping
    public ResponseEntity<List<PartyCardResponse>> getMyParties() {
        return ResponseEntity.ok(partyService.getMyParties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartyResponse> getPartyById(@PathVariable Long id) {
        return ResponseEntity.ok(partyService.getPartyById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartyResponse> updateParty(@PathVariable Long id,
                                                     @RequestBody UpdatePartyRequest request) {
        return ResponseEntity.ok(partyService.updateParty(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParty(@PathVariable Long id) {
        partyService.deleteParty(id);
        return ResponseEntity.noContent().build();
    }
}