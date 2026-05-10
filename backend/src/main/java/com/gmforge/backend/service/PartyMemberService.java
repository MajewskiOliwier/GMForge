package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.AddPartyMemberRequest;
import com.gmforge.backend.dto.response.PartyMemberResponse;
import com.gmforge.backend.entity.Party;
import com.gmforge.backend.entity.PartyMember;
import com.gmforge.backend.entity.User;
import com.gmforge.backend.repository.PartyMemberRepository;
import com.gmforge.backend.repository.PartyRepository;
import com.gmforge.backend.repository.UserRepository;
import com.gmforge.backend.util.PartyAuthHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyMemberService {

    private final PartyMemberRepository partyMemberRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PartyAuthHelper partyAuthHelper;

    public PartyMemberService(PartyMemberRepository partyMemberRepository,
                              PartyRepository partyRepository,
                              UserRepository userRepository,
                              UserService userService, PartyAuthHelper partyAuthHelper) {
        this.partyMemberRepository = partyMemberRepository;
        this.partyRepository = partyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.partyAuthHelper = partyAuthHelper;
    }

    public PartyMemberResponse addMember(Long partyId, AddPartyMemberRequest request) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));
        partyAuthHelper.assertIsGameMaster(party);

        User userToAdd = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (partyMemberRepository.existsByPartyAndUser(party, userToAdd)) {
            throw new RuntimeException("User is already a member of this party");
        }

        PartyMember member = new PartyMember();
        member.setParty(party);
        member.setUser(userToAdd);
        partyMemberRepository.save(member);

        return mapToResponse(member);
    }

    public List<PartyMemberResponse> getMembers(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));

        PartyMemberResponse gm = new PartyMemberResponse();
        gm.setId(party.getGameMaster().getId());
        gm.setUsername(party.getGameMaster().getUsername());
        gm.setEmail(party.getGameMaster().getEmail());
        gm.setGameMaster(true);

        List<PartyMemberResponse> members = new java.util.ArrayList<>();
        members.add(gm);

        partyMemberRepository.findAllByParty(party)
                .stream()
                .map(this::mapToResponse)
                .forEach(members::add);

        return members;
    }

    public void removeMember(Long partyId, Long userId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));
        User currentUser = userService.getCurrentUser();
        boolean isGm = party.getGameMaster().getId().equals(currentUser.getId());
        boolean isSelf = currentUser.getId().equals(userId);

        if (!isGm && !isSelf) {
            throw new RuntimeException("Not authorized to remove this member");
        }

        User userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PartyMember member = partyMemberRepository.findAllByParty(party)
                .stream()
                .filter(m -> m.getUser().getId().equals(userToRemove.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User is not a member of this party"));

        partyMemberRepository.delete(member);
    }

    private PartyMemberResponse mapToResponse(PartyMember member) {
        PartyMemberResponse response = new PartyMemberResponse();
        response.setId(member.getUser().getId());
        response.setUsername(member.getUser().getUsername());
        response.setEmail(member.getUser().getEmail());
        response.setGameMaster(false);
        return response;
    }
}