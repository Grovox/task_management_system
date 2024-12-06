package com.example.task.management.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Entity комментария.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    /**
     * id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;

    /**
     * Текст комментария.
     */
    @Column(name = "description")
    private String description;

    /**
     * Автор комментария.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * Задача для комментария.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private Task task;
}
