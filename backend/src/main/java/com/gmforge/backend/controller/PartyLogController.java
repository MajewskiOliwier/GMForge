package com.gmforge.backend.controller;

import com.gmforge.backend.dto.request.CreatePartyLogRequest;
import com.gmforge.backend.dto.request.DeleteLogsRequest;
import com.gmforge.backend.dto.response.PartyLogResponse;
import com.gmforge.backend.service.PartyLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/party-log")
public class PartyLogController {

    private final PartyLogService partyLogService;

    public PartyLogController(PartyLogService partyLogService) {
        this.partyLogService = partyLogService;
    }

    @PostMapping("/{partyId}")
    public ResponseEntity<PartyLogResponse> createLog(@PathVariable Long partyId,
                                                      @RequestBody CreatePartyLogRequest request) {
        return ResponseEntity.ok(partyLogService.createLog(partyId, request));
    }

    @GetMapping("/{partyId}")
    public ResponseEntity<List<PartyLogResponse>> getLogs(@PathVariable Long partyId) {
        return ResponseEntity.ok(partyLogService.getLogs(partyId));
    }

    @DeleteMapping("/{partyId}/trim")
    public ResponseEntity<Void> deleteOldest(@PathVariable Long partyId,
                                             @RequestBody DeleteLogsRequest request) {
        partyLogService.deleteOldestLogs(partyId, request.getCount());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{partyId}/count")
    public ResponseEntity<Long> getLogsCount(@PathVariable Long partyId) {
        return ResponseEntity.ok(partyLogService.getLogsCount(partyId));
    }
}