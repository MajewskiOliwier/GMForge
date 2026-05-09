package com.gmforge.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "character_item")
@EntityListeners(AuditingEntityListener.class)
public class CharacterItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private Boolean isHidden = false;

    @Column(nullable = true)
    private String hiddenName;

    @Column(nullable = true)
    private String hiddenDescription;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}