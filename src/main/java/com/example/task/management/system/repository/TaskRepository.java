package com.example.task.management.system.repository;

import com.example.task.management.system.entity.Task;
import com.example.task.management.system.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

import java.util.UUID;

/**
 * Репозитория для работы с сущностью задачи.
 *
 * @author Max Artremov
 */
public interface TaskRepository  extends JpaRepository<Task, UUID> {

    /**
     * Возвращяет все задачи.
     */
    @EntityGraph(attributePaths = {"author", "performer", "comments", "comments.author"})
    List<Task> findAll();


    /**
     * Возвращяет все задачи автора.
     */
    @EntityGraph(attributePaths = {"author", "performer", "comments", "comments.author"})
    List<Task> findByAuthor(User author);


    /**
     * Возвращяет все задачи исполнителя.
     */
    @EntityGraph(attributePaths = {"author", "performer", "comments", "comments.author"})
    List<Task> findByPerformer(User performer);
}
