package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.CreateSessionRequest;
import com.gmforge.backend.dto.response.SessionResponse;
import com.gmforge.backend.entity.Party;
import com.gmforge.backend.entity.Session;
import com.gmforge.backend.repository.PartyRepository;
import com.gmforge.backend.repository.SessionRepository;
import com.gmforge.backend.util.PartyAuthHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final PartyRepository partyRepository;
    private final PartyAuthHelper partyAuthHelper;

    public SessionService(SessionRepository sessionRepository,
                          PartyRepository partyRepository,
                          UserService userService, PartyAuthHelper partyAuthHelper) {
        this.sessionRepository = sessionRepository;
        this.partyRepository = partyRepository;
        this.partyAuthHelper = partyAuthHelper;
    }

    public SessionResponse createSession(Long partyId, CreateSessionRequest request) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));
        partyAuthHelper.assertIsGameMaster(party);

        List<Session> existing = sessionRepository.findAllByPartyOrderBySessionNumberAsc(party);
        int nextNumber = existing.isEmpty() ? 1 : existing.get(existing.size() - 1).getSessionNumber() + 1;

        Session session = new Session();
        session.setParty(party);
        session.setName(request.getName());
        session.setSessionNumber(nextNumber);
        sessionRepository.save(session);

        return mapToResponse(session);
    }

    public List<SessionResponse> getSessions(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));
        return sessionRepository.findAllByPartyOrderBySessionNumberAsc(party)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        partyAuthHelper.assertIsGameMaster(session.getParty());
        sessionRepository.delete(session);
    }

    private SessionResponse mapToResponse(Session session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setName(session.getName());
        response.setSessionNumber(session.getSessionNumber());
        response.setCreatedAt(session.getCreatedAt());
        return response;
    }
}