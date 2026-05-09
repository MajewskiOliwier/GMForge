package com.gmforge.backend.repository;

import com.gmforge.backend.entity.Character;
import com.gmforge.backend.entity.Party;
import com.gmforge.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    List<Character> findAllByParty(Party party);
    List<Character> findAllByPlayer(User player);
    List<Character> findAllByPlayerIsNull(); // all NPCs
    List<Character> findAllByPartyAndPlayerIsNull(Party party); // NPCs in party
}