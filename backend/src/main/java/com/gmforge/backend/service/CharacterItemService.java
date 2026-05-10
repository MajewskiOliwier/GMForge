package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.CreateAndAssignItemRequest;
import com.gmforge.backend.dto.request.UpdateCharacterItemRequest;
import com.gmforge.backend.dto.request.UpdateHiddenItemRequest;
import com.gmforge.backend.dto.response.CharacterItemResponse;
import com.gmforge.backend.entity.*;
import com.gmforge.backend.entity.Character;
import com.gmforge.backend.repository.*;
import com.gmforge.backend.util.PartyAuthHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterItemService {

    private final CharacterItemRepository characterItemRepository;
    private final CharacterRepository characterRepository;
    private final ItemRepository itemRepository;
    private final PartyAuthHelper partyAuthHelper;

    public CharacterItemService(CharacterItemRepository characterItemRepository,
                                CharacterRepository characterRepository,
                                ItemRepository itemRepository, PartyAuthHelper partyAuthHelper) {
        this.characterItemRepository = characterItemRepository;
        this.characterRepository = characterRepository;
        this.itemRepository = itemRepository;
        this.partyAuthHelper = partyAuthHelper;
    }

    public CharacterItemResponse addItem(Long characterId, CreateAndAssignItemRequest request) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));

        boolean isGm = partyAuthHelper.isGameMaster(character.getParty());

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

        partyAuthHelper.assertIsGameMaster(characterItem.getCharacter().getParty());

        characterItem.setIsHidden(request.getIsHidden() != null ? request.getIsHidden() : false);
        characterItem.setHiddenName(request.getHiddenName());
        characterItem.setHiddenDescription(request.getHiddenDescription());
        characterItemRepository.save(characterItem);

        return mapToResponse(characterItem);
    }

    public CharacterItemResponse updateItem(Long id, UpdateCharacterItemRequest request) {
        CharacterItem characterItem = characterItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character item not found"));
        partyAuthHelper.assertIsGmOrOwner(characterItem.getCharacter());

        Item item = characterItem.getItem();
        if (request.getName() != null) item.setName(request.getName());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        itemRepository.save(item);

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