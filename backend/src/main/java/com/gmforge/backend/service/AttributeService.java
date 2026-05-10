package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.CreateAttributeRequest;
import com.gmforge.backend.dto.request.character.UpdateAttributeRequest;
import com.gmforge.backend.dto.response.CharacterAttributeResponse;
import com.gmforge.backend.entity.Character;
import com.gmforge.backend.entity.CharacterAttribute;
import com.gmforge.backend.repository.CharacterAttributeRepository;
import com.gmforge.backend.repository.CharacterRepository;
import com.gmforge.backend.util.PartyAuthHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeService {

    private final CharacterAttributeRepository characterAttributeRepository;
    private final CharacterRepository characterRepository;
    private final PartyAuthHelper partyAuthHelper;

    public AttributeService(CharacterAttributeRepository characterAttributeRepository,
                            CharacterRepository characterRepository, UserService userService, PartyAuthHelper partyAuthHelper) {
        this.characterAttributeRepository = characterAttributeRepository;
        this.characterRepository = characterRepository;
        this.partyAuthHelper = partyAuthHelper;
    }

    public CharacterAttributeResponse addAttribute(Long characterId, CreateAttributeRequest request) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        partyAuthHelper.assertIsGmOrOwner(character);

        CharacterAttribute attribute = new CharacterAttribute();
        attribute.setCharacter(character);
        attribute.setSkill(request.getSkill());
        attribute.setStyle(request.getStyle());
        attribute.setLevel(request.getLevel());
        characterAttributeRepository.save(attribute);

        return mapToResponse(attribute);
    }

    public List<CharacterAttributeResponse> getAttributes(Long characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        return characterAttributeRepository.findAllByCharacter(character)
                .stream().map(this::mapToResponse).toList();
    }

    public List<CharacterAttributeResponse> getSkills(Long characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        return characterAttributeRepository.findAllByCharacterAndSkillIsNotNull(character)
                .stream().map(this::mapToResponse).toList();
    }

    public List<CharacterAttributeResponse> getStyles(Long characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        return characterAttributeRepository.findAllByCharacterAndStyleIsNotNull(character)
                .stream().map(this::mapToResponse).toList();
    }

    public CharacterAttributeResponse updateAttribute(Long id, UpdateAttributeRequest request) {
        CharacterAttribute attribute = characterAttributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found"));
        partyAuthHelper.assertIsGmOrOwner(attribute.getCharacter());
        attribute.setSkill(request.getSkill());
        attribute.setStyle(request.getStyle());
        attribute.setLevel(request.getLevel());
        characterAttributeRepository.save(attribute);
        return mapToResponse(attribute);
    }

    private CharacterAttributeResponse mapToResponse(CharacterAttribute attribute) {
        CharacterAttributeResponse response = new CharacterAttributeResponse();
        response.setId(attribute.getId());
        response.setSkill(attribute.getSkill());
        response.setStyle(attribute.getStyle());
        response.setLevel(attribute.getLevel());
        return response;
    }
}