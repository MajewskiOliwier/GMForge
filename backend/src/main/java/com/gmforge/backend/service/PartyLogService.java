package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.CreatePartyLogRequest;
import com.gmforge.backend.dto.response.PartyLogResponse;
import com.gmforge.backend.entity.Party;
import com.gmforge.backend.entity.PartyLog;
import com.gmforge.backend.entity.User;
import com.gmforge.backend.repository.PartyLogRepository;
import com.gmforge.backend.repository.PartyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyLogService {

    private final PartyLogRepository partyLogRepository;
    private final PartyRepository partyRepository;
    private final UserService userService;

    public PartyLogService(PartyLogRepository partyLogRepository,
                           PartyRepository partyRepository,
                           UserService userService) {
        this.partyLogRepository = partyLogRepository;
        this.partyRepository = partyRepository;
        this.userService = userService;
    }

    public PartyLogResponse createLog(Long partyId, CreatePartyLogRequest request) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));
        User currentUser = userService.getCurrentUser();

        PartyLog log = new PartyLog();
        log.setParty(party);
        log.setCreator(currentUser);
        log.setDescription(request.getDescription());
        partyLogRepository.save(log);

        return mapToResponse(log);
    }

    public List<PartyLogResponse> getLogs(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));

        return partyLogRepository.findAllByPartyOrderByCreatedAtDesc(party)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteOldestLogs(Long partyId, int count) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));

        List<PartyLog> oldest = partyLogRepository.findTopNByPartyOrderByCreatedAtAsc(party, PageRequest.of(0, count));
        partyLogRepository.deleteAll(oldest);
    }

    public long getLogsCount(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));

        return partyLogRepository.countByParty(party);
    }

    private PartyLogResponse mapToResponse(PartyLog log) {
        PartyLogResponse response = new PartyLogResponse();
        response.setId(log.getId());
        response.setDescription(log.getDescription());
        response.setCreatedByUsername(log.getCreator().getUsername());
        response.setCreatedAt(log.getCreatedAt());

        return response;
    }
}