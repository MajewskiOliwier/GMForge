package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.CreateAndAssignItemRequest;
import com.gmforge.backend.dto.request.UpdateHiddenItemRequest;
import com.gmforge.backend.dto.response.CharacterItemResponse;
import com.gmforge.backend.entity.*;
import com.gmforge.backend.entity.Character;
import com.gmforge.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterItemService {

    private final CharacterItemRepository characterItemRepository;
    private final CharacterRepository characterRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    public CharacterItemService(CharacterItemRepository characterItemRepository,
                                CharacterRepository characterRepository,
                                ItemRepository itemRepository,
                                UserService userService) {
        this.characterItemRepository = characterItemRepository;
        this.characterRepository = characterRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    public CharacterItemResponse addItem(Long characterId, CreateAndAssignItemRequest request) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        boolean isGm = character.getParty().getGameMaster().getId()
                .equals(userService.getCurrentUser().getId());

        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setType(request.getType());
        itemRepository.save(item);

        CharacterItem characterItem = new CharacterItem();
        characterItem.setCharacter(character);
        characterItem.setItem(item);

        if (isGm) {
            characterItem.setIsHidden(request.getIsHidden() != null ? request.getIsHidden() : false);
            characterItem.setHiddenName(request.getHiddenName());
            characterItem.setHiddenDescription(request.getHiddenDescription());
        } else {
            characterItem.setIsHidden(false);
        }

        characterItemRepository.save(characterItem);
        return mapToResponse(characterItem);
    }

    public List<CharacterItemResponse> getItems(Long characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        return characterItemRepository.findAllByCharacter(character)
                .stream().map(this::mapToResponse).toList();
    }

    public CharacterItemResponse updateHidden(Long id, UpdateHiddenItemRequest request) {
        CharacterItem characterItem = characterItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character item not found"));
        User currentUser = userService.getCurrentUser();
        if (!characterItem.getCharacter().getParty().getGameMaster().getId()
                .equals(currentUser.getId())) {
            throw new RuntimeException("Only the GM can update hidden fields");
        }
        characterItem.setIsHidden(request.getIsHidden() != null ? request.getIsHidden() : false);
        characterItem.setHiddenName(request.getHiddenName());
        characterItem.setHiddenDescription(request.getHiddenDescription());
        characterItemRepository.save(characterItem);
        return mapToResponse(characterItem);
    }

    public void removeItem(Long id) {
        CharacterItem characterItem = characterItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character item not found"));
        characterItemRepository.delete(characterItem);
    }

    private CharacterItemResponse mapToResponse(CharacterItem characterItem) {
        CharacterItemResponse response = new CharacterItemResponse();
        response.setId(characterItem.getId());
        response.setName(characterItem.getItem().getName());
        response.setDescription(characterItem.getItem().getDescription());
        response.setType(characterItem.getItem().getType());
        response.setIsHidden(characterItem.getIsHidden());
        response.setHiddenName(characterItem.getHiddenName());
        response.setHiddenDescription(characterItem.getHiddenDescription());
        return response;
    }
}