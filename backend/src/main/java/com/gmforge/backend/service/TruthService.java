package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.CreateTruthRequest;
import com.gmforge.backend.dto.request.UpdateTruthRequest;
import com.gmforge.backend.dto.response.TruthResponse;
import com.gmforge.backend.entity.Character;
import com.gmforge.backend.entity.Truth;
import com.gmforge.backend.repository.CharacterRepository;
import com.gmforge.backend.repository.TruthRepository;
import com.gmforge.backend.util.PartyAuthHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruthService {

    private final TruthRepository truthRepository;
    private final CharacterRepository characterRepository;
    private final PartyAuthHelper partyAuthHelper ;

    public TruthService(TruthRepository truthRepository,
                        CharacterRepository characterRepository, UserService userService, PartyAuthHelper partyAuthHelper) {
        this.truthRepository = truthRepository;
        this.characterRepository = characterRepository;
        this.partyAuthHelper = partyAuthHelper;
    }

    public TruthResponse addTruth(Long characterId, CreateTruthRequest request) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));

        Truth truth = new Truth();
        truth.setCharacter(character);
        truth.setName(request.getName());
        truthRepository.save(truth);

        return mapToResponse(truth);
    }

    public List<TruthResponse> getTruths(Long characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        return truthRepository.findAllByCharacter(character)
                .stream().map(this::mapToResponse).toList();
    }

    public TruthResponse updateTruth(Long id, UpdateTruthRequest request) {
        Truth truth = truthRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Truth not found"));
        truth.setName(request.getName());
        truthRepository.save(truth);
        return mapToResponse(truth);
    }

    public void deleteTruth(Long id) {
        Truth truth = truthRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Truth not found"));
        partyAuthHelper.assertIsGmOrOwner(truth.getCharacter());
        truthRepository.delete(truth);
    }

    private TruthResponse mapToResponse(Truth truth) {
        TruthResponse response = new TruthResponse();
        response.setId(truth.getId());
        response.setName(truth.getName());
        return response;
    }
}