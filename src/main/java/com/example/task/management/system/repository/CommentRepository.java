package com.example.task.management.system.repository;

import com.example.task.management.system.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Репозитория для работы с сущьностю комментария.
 *
 * @author Max Artremov
 */
public interface CommentRepository  extends JpaRepository<Comment, UUID> {

}
