package com.gmforge.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "focus")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Focus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private Character character;

    @Min(2)
    @Max(5)
    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private String text;
}
