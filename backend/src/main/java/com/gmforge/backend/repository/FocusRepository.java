package com.gmforge.backend.repository;

import com.gmforge.backend.entity.Character;
import com.gmforge.backend.entity.Focus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FocusRepository extends JpaRepository<Focus, Long> {
    List<Focus> findAllByCharacter(Character character);
}