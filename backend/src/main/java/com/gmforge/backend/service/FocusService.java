package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.CreateFocusRequest;
import com.gmforge.backend.dto.request.character.UpdateFocusRequest;
import com.gmforge.backend.dto.response.FocusResponse;
import com.gmforge.backend.entity.Character;
import com.gmforge.backend.entity.Focus;
import com.gmforge.backend.repository.CharacterRepository;
import com.gmforge.backend.repository.FocusRepository;
import com.gmforge.backend.util.PartyAuthHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FocusService {

    private final FocusRepository focusRepository;
    private final CharacterRepository characterRepository;
    private final PartyAuthHelper partyAuthHelper;

    public FocusService(FocusRepository focusRepository,
                        CharacterRepository characterRepository, PartyAuthHelper partyAuthHelper) {
        this.focusRepository = focusRepository;
        this.characterRepository = characterRepository;
        this.partyAuthHelper = partyAuthHelper;
    }

    public FocusResponse addFocus(Long characterId, CreateFocusRequest request) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        partyAuthHelper.assertIsGmOrOwner(character);

        Focus focus = new Focus();
        focus.setCharacter(character);
        focus.setLevel(request.getLevel());
        focus.setText(request.getText());
        focusRepository.save(focus);

        return mapToResponse(focus);
    }

    public List<FocusResponse> getFocuses(Long characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        return focusRepository.findAllByCharacter(character)
                .stream().map(this::mapToResponse).toList();
    }

    public FocusResponse updateFocus(Long id, UpdateFocusRequest request) {
        Focus focus = focusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Focus not found"));
        focus.setLevel(request.getLevel());
        focus.setText(request.getText());
        focusRepository.save(focus);
        return mapToResponse(focus);
    }

    public void deleteFocus(Long id) {
        Focus focus = focusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Focus not found"));
        focusRepository.delete(focus);
    }

    private FocusResponse mapToResponse(Focus focus) {
        FocusResponse response = new FocusResponse();
        response.setId(focus.getId());
        response.setLevel(focus.getLevel());
        response.setText(focus.getText());
        return response;
    }
}