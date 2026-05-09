package com.gmforge.backend.repository;

import com.gmforge.backend.entity.Character;
import com.gmforge.backend.entity.CharacterItem;
import com.gmforge.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CharacterItemRepository extends JpaRepository<CharacterItem, Long> {
    List<CharacterItem> findAllByCharacter(Character character);
    List<CharacterItem> findAllByItem(Item item);
    boolean existsByCharacterAndItem(Character character, Item item);
}