package com.gmforge.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "game_character") //table is named differently than class due to Mysql reserving name "character"
@EntityListeners(AuditingEntityListener.class)
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User player; // if null then it is NPC

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "character")
    private List<CharacterAttribute> attributes;

    @OneToMany(mappedBy = "character")
    private List<Focus> focuses;

    @OneToMany(mappedBy = "character")
    private List<Truth> truths;

    @OneToMany(mappedBy = "character")
    private List<CharacterItem> items;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}