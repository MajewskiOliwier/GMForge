package com.gmforge.backend.repository;

import com.gmforge.backend.entity.Item;
import com.gmforge.backend.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByType(ItemType type);
    boolean existsByName(String name);
}