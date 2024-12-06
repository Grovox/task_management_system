package com.example.task.management.system.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity токена доступа.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
