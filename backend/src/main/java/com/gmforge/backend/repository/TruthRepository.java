package com.gmforge.backend.repository;

import com.gmforge.backend.entity.Character;
import com.gmforge.backend.entity.Truth;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TruthRepository extends JpaRepository<Truth, Long> {
    List<Truth> findAllByCharacter(Character character);
}