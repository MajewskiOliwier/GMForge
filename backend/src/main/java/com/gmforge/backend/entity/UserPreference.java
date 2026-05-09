package com.gmforge.backend.entity;

import com.gmforge.backend.enums.Theme;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_preference")
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Theme theme = Theme.DARK;
}
