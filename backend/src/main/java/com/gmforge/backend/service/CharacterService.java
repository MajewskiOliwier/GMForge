package com.gmforge.backend.service;

import com.gmforge.backend.dto.request.*;
import com.gmforge.backend.dto.request.character.UpdateCharacterNameRequest;
import com.gmforge.backend.dto.response.*;
import com.gmforge.backend.entity.*;
import com.gmforge.backend.entity.Character;
import com.gmforge.backend.repository.*;
import com.gmforge.backend.util.PartyAuthHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final CharacterAttributeRepository characterAttributeRepository;
    private final FocusRepository focusRepository;
    private final TruthRepository truthRepository;
    private final CharacterItemRepository characterItemRepository;
    private final UserService userService;
    private final PartyAuthHelper partyAuthHelper;
    private final ItemRepository itemRepository;

    public CharacterService(CharacterRepository characterRepository,
                            PartyRepository partyRepository,
                            UserRepository userRepository,
                            CharacterAttributeRepository characterAttributeRepository,
                            FocusRepository focusRepository,
                            TruthRepository truthRepository,
                            CharacterItemRepository characterItemRepository,
                            UserService userService, PartyAuthHelper partyAuthHelper, ItemRepository itemRepository) {
        this.characterRepository = characterRepository;
        this.partyRepository = partyRepository;
        this.userRepository = userRepository;
        this.characterAttributeRepository = characterAttributeRepository;
        this.focusRepository = focusRepository;
        this.truthRepository = truthRepository;
        this.characterItemRepository = characterItemRepository;
        this.userService = userService;
        this.partyAuthHelper = partyAuthHelper;
        this.itemRepository = itemRepository;
    }

    public CharacterResponse createCharacter(Long partyId, CreateCharacterRequest request) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));

        partyAuthHelper.assertIsGameMasterOrLoggedUser(party, request.getUserId());

        Character character = new Character();
        character.setParty(party);
        character.setName(request.getName());

        User currentUser = userService.getCurrentUser();
        boolean isGm = party.getGameMaster().getId().equals(currentUser.getId());

        System.out.println("request id = "+request.getUserId() + ", gm id = "+party.getGameMaster().getId()+ " , current user = "+currentUser.getId());
//
        if (request.getUserId() == null) {
            throw new RuntimeException("Only the GM can create NPCs");
        }

        characterRepository.save(character);

        if (request.getAttributes() != null) {
            for (CreateAttributeRequest a : request.getAttributes()) {
                CharacterAttribute attribute = new CharacterAttribute();
                attribute.setCharacter(character);
                attribute.setSkill(a.getSkill());
                attribute.setStyle(a.getStyle());
                attribute.setLevel(a.getLevel());
                characterAttributeRepository.save(attribute);
            }
        }

        if (request.getFocuses() != null) {
            for (CreateFocusRequest f : request.getFocuses()) {
                Focus focus = new Focus();
                focus.setCharacter(character);
                focus.setLevel(f.getLevel());
                focus.setText(f.getText());
                focusRepository.save(focus);
            }
        }

        if (request.getItems() != null) {
            for (CreateAndAssignItemRequest i : request.getItems()) {
                Item item = new Item();
                item.setName(i.getName());
                item.setDescription(i.getDescription());
                item.setType(i.getType());
                itemRepository.save(item);

                CharacterItem characterItem = new CharacterItem();
                characterItem.setCharacter(character);
                characterItem.setItem(item);

                if (isGm) {
                    characterItem.setIsHidden(i.getIsHidden() != null ? i.getIsHidden() : false);
                    characterItem.setHiddenName(i.getHiddenName());
                    characterItem.setHiddenDescription(i.getHiddenDescription());
                } else {
                    characterItem.setIsHidden(false);
                }

                characterItemRepository.save(characterItem);
            }
        }

        return mapToResponse(character);
    }

    public List<CharacterResponse> getCharacters(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));
        return characterRepository.findAllByParty(party)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<CharacterResponse> getNpcs(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));
        return characterRepository.findAllByPartyAndPlayerIsNull(party)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<CharacterResponse> getPlayers(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));
        return characterRepository.findAllByParty(party)
                .stream()
                .filter(c -> c.getPlayer() != null)
                .map(this::mapToResponse)
                .toList();
    }

    public CharacterResponse getCharacterById(Long id) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        return mapToResponse(character);
    }

    public CharacterResponse updateCharacterName(Long id, UpdateCharacterNameRequest request) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        partyAuthHelper.assertIsGameMaster(character.getParty());
        character.setName(request.getName());
        characterRepository.save(character);
        return mapToResponse(character);
    }

    public void deleteCharacter(Long id) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        partyAuthHelper.assertIsGameMaster(character.getParty());
        characterRepository.delete(character);
    }

    private CharacterResponse mapToResponse(Character character) {
        CharacterResponse response = new CharacterResponse();
        response.setId(character.getId());
        response.setName(character.getName());
        response.setNpc(character.getPlayer() == null);
        response.setPlayerUsername(character.getPlayer() != null
                ? character.getPlayer().getUsername() : null);

        response.setAttributes(characterAttributeRepository.findAllByCharacter(character)
                .stream()
                .map(a -> {
                    CharacterAttributeResponse ar = new CharacterAttributeResponse();
                    ar.setId(a.getId());
                    ar.setSkill(a.getSkill());
                    ar.setStyle(a.getStyle());
                    ar.setLevel(a.getLevel());
                    return ar;
                }).toList());

        response.setFocuses(focusRepository.findAllByCharacter(character)
                .stream()
                .map(f -> {
                    FocusResponse fr = new FocusResponse();
                    fr.setId(f.getId());
                    fr.setLevel(f.getLevel());
                    fr.setText(f.getText());
                    return fr;
                }).toList());

        response.setTruths(truthRepository.findAllByCharacter(character)
                .stream()
                .map(t -> {
                    TruthResponse tr = new TruthResponse();
                    tr.setId(t.getId());
                    tr.setName(t.getName());
                    return tr;
                }).toList());

        response.setItems(characterItemRepository.findAllByCharacter(character)
                .stream()
                .map(ci -> {
                    CharacterItemResponse cir = new CharacterItemResponse();
                    cir.setId(ci.getId());
                    cir.setName(ci.getItem().getName());
                    cir.setDescription(ci.getItem().getDescription());
                    cir.setType(ci.getItem().getType());
                    cir.setIsHidden(ci.getIsHidden());
                    cir.setHiddenName(ci.getHiddenName());
                    cir.setHiddenDescription(ci.getHiddenDescription());
                    return cir;
                }).toList());

        return response;
    }
}