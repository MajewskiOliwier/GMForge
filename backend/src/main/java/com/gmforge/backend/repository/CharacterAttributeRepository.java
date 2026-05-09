package com.gmforge.backend.repository;

import com.gmforge.backend.entity.Character;
import com.gmforge.backend.entity.CharacterAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CharacterAttributeRepository extends JpaRepository<CharacterAttribute, Long> {
    List<CharacterAttribute> findAllByCharacter(Character character);

    //Get all skills
    List<CharacterAttribute> findAllByCharacterAndSkillIsNotNull(Character character);

    //Get all styles
    List<CharacterAttribute> findAllByCharacterAndStyleIsNotNull(Character character);

    //will be for debugs to check if there is exactly 6 skills and 6 styles
    int countByCharacterAndSkillIsNotNull(Character character);
    int countByCharacterAndStyleIsNotNull(Character character);
}