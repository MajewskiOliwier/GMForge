package com.gmforge.backend.entity;

import com.gmforge.backend.enums.SkillType;
import com.gmforge.backend.enums.StyleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
@Table(name = "character_attribute")
public class CharacterAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //either skilltype or styletype can have value at any given time
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private SkillType skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private StyleType style;

    @Min(4)
    @Max(8)
    @Column(nullable = false)
    private int level;

    @ManyToOne
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;
}