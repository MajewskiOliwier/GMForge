package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.CreateSessionRequest;
import com.gmforge.backend.dto.response.SessionResponse;
import com.gmforge.backend.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/{partyId}")
    public ResponseEntity<SessionResponse> createSession(@PathVariable Long partyId,
                                                         @RequestBody CreateSessionRequest request) {
        return ResponseEntity.ok(sessionService.createSession(partyId, request));
    }

    @GetMapping("/{partyId}")
    public ResponseEntity<List<SessionResponse>> getSessions(@PathVariable Long partyId) {
        return ResponseEntity.ok(sessionService.getSessions(partyId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}