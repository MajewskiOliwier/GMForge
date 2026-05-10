package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.AddPartyMemberRequest;
import com.gmforge.backend.dto.response.PartyMemberResponse;
import com.gmforge.backend.service.PartyMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/party-member")
public class PartyMemberController {

    private final PartyMemberService partyMemberService;

    public PartyMemberController(PartyMemberService partyMemberService) {
        this.partyMemberService = partyMemberService;
    }

    @PostMapping("/{partyId}")
    public ResponseEntity<PartyMemberResponse> addMember(@PathVariable Long partyId,
                                                         @RequestBody AddPartyMemberRequest request) {
        return ResponseEntity.ok(partyMemberService.addMember(partyId, request));
    }

    @GetMapping("/{partyId}")
    public ResponseEntity<List<PartyMemberResponse>> getMembers(@PathVariable Long partyId) {
        return ResponseEntity.ok(partyMemberService.getMembers(partyId));
    }

    @DeleteMapping("/{partyId}/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long partyId,
                                             @PathVariable Long userId) {
        partyMemberService.removeMember(partyId, userId);
        return ResponseEntity.noContent().build();
    }
}