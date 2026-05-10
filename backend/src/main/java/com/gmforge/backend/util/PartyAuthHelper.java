package com.gmforge.backend.util;

import com.gmforge.backend.entity.Character;
import com.gmforge.backend.entity.Party;
import com.gmforge.backend.entity.User;
import com.gmforge.backend.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class PartyAuthHelper {

    private final UserService userService;

    public PartyAuthHelper(UserService userService) {
        this.userService = userService;
    }

    public void assertIsGameMaster(Party party) {
        User currentUser = userService.getCurrentUser();

        if (!party.getGameMaster().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only the GM can perform this action");
        }
    }

    public void assertIsGameMasterOrLoggedUser(Party party, Long userId) {
        User currentUser = userService.getCurrentUser();

        boolean isGm = party.getGameMaster().getId().equals(currentUser.getId());
        boolean isSelf = userId != null && currentUser.getId().equals(userId);

        if (!isGm && !isSelf) {
            throw new RuntimeException("Only the GM or the player themselves can perform this action");
        }
    }

    public void assertIsGmOrOwner(Character character) {
        User currentUser = userService.getCurrentUser();
        boolean isGm = character.getParty().getGameMaster().getId()
                .equals(currentUser.getId());
        boolean isOwner = character.getPlayer() != null &&
                character.getPlayer().getId().equals(currentUser.getId());
        if (!isGm && !isOwner) {
            throw new RuntimeException("Only the GM or character owner can perform this action");
        }
    }

    public boolean isGameMaster(Party party) {
        User currentUser = userService.getCurrentUser();
        return party.getGameMaster().getId().equals(currentUser.getId());
    }
}