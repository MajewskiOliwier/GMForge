package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.CreatePartyRequest;
import com.gmforge.backend.dto.request.UpdatePartyRequest;
import com.gmforge.backend.dto.response.PartyCardResponse;
import com.gmforge.backend.dto.response.PartyResponse;
import com.gmforge.backend.dto.response.SessionResponse;
import com.gmforge.backend.dto.response.UserResponse;
import com.gmforge.backend.entity.Party;
import com.gmforge.backend.entity.Session;
import com.gmforge.backend.entity.User;
import com.gmforge.backend.repository.PartyMemberRepository;
import com.gmforge.backend.repository.PartyRepository;
import com.gmforge.backend.repository.SessionRepository;
import com.gmforge.backend.util.PartyAuthHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final PartyAuthHelper partyAuthHelper;

    public PartyService(PartyRepository partyRepository,
                        PartyMemberRepository partyMemberRepository,
                        SessionRepository sessionRepository,
                        UserService userService, PartyAuthHelper partyAuthHelper) {
        this.partyRepository = partyRepository;
        this.partyMemberRepository = partyMemberRepository;
        this.sessionRepository = sessionRepository;
        this.userService = userService;
        this.partyAuthHelper = partyAuthHelper;
    }

    public PartyResponse createParty(CreatePartyRequest request) {
        User currentUser = userService.getCurrentUser();
        Party party = new Party();
        party.setName(request.getName());
        party.setGameMaster(currentUser);
        partyRepository.save(party);
        return mapToResponse(party);
    }

    public List<PartyCardResponse> getMyParties() {
        User currentUser = userService.getCurrentUser();
        return partyRepository.findAllByGameMaster(currentUser)
                .stream()
                .map(this::mapToCardResponse)
                .toList();
    }

    public PartyResponse getPartyById(Long id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Party not found"));
        return mapToResponse(party);
    }

    public PartyResponse updateParty(Long id, UpdatePartyRequest request) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Party not found"));
        partyAuthHelper.assertIsGameMaster(party);
        party.setName(request.getName());
        partyRepository.save(party);
        return mapToResponse(party);
    }

    public void deleteParty(Long id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Party not found"));
        partyAuthHelper.assertIsGameMaster(party);
        partyRepository.delete(party);
    }

    private PartyResponse mapToResponse(Party party) {
        List<Session> sessions = sessionRepository.findAllByPartyOrderBySessionNumberAsc(party);
        Session latest = sessions.isEmpty() ? null : sessions.get(sessions.size() - 1);

        UserResponse gmResponse = new UserResponse();
        gmResponse.setId(party.getGameMaster().getId());
        gmResponse.setUsername(party.getGameMaster().getUsername());
        gmResponse.setEmail(party.getGameMaster().getEmail());

        PartyResponse response = new PartyResponse();
        response.setId(party.getId());
        response.setName(party.getName());
        response.setGameMaster(gmResponse);
        response.setMemberCount(partyMemberRepository.findAllByParty(party).size());
        response.setCurrentSession(latest != null ? mapToSessionResponse(latest) : null);
        return response;
    }

    private PartyCardResponse mapToCardResponse(Party party) {
        List<Session> sessions = sessionRepository.findAllByPartyOrderBySessionNumberAsc(party);
        Session latest = sessions.isEmpty() ? null : sessions.get(sessions.size() - 1);

        PartyCardResponse response = new PartyCardResponse();
        response.setId(party.getId());
        response.setName(party.getName());
        response.setGameMasterUsername(party.getGameMaster().getUsername());
        response.setMemberCount(partyMemberRepository.findAllByParty(party).size());
        response.setCurrentSessionName(latest != null ? latest.getName() : null);
        response.setCurrentSessionNumber(latest != null ? latest.getSessionNumber() : 0);
        return response;
    }

    private SessionResponse mapToSessionResponse(Session session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setName(session.getName());
        response.setSessionNumber(session.getSessionNumber());
        response.setCreatedAt(session.getCreatedAt());
        return response;
    }
}