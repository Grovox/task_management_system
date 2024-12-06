package com.example.task.management.system.entity;

import com.example.task.management.system.entity.enums.Priority;
import com.example.task.management.system.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Entity задачи.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    /**
     * id.
     */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "id")
    private UUID id;

    /**
     * Заголовок.
     */
    @Column(name = "title")
    private String title;

    /**
     * Статус задачи.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * Приоритет задачи.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    /**
     * Описание задачи.
     */
    @Column(name = "description")
    private String description;

    /**
     * Автор задачи.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_creator_id")
    private User author;

    /**
     * Исполнитель задачи.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_performer_id")
    private User performer;

    /**
     * Комментарии к задачи.
     */
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
