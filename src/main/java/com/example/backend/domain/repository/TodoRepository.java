package com.example.backend.domain.repository;

import java.util.Collection;
import java.util.Optional;

import com.example.backend.domain.model.Todo;

/**
 * TodoのRepositoryインタフェース 
 */
public interface TodoRepository {
    Optional<Todo> findById(String todoId);

    Collection<Todo> findAll();

    void create(Todo todo);

    boolean update(Todo todo);

    void delete(Todo todo);

    long countByFinished(boolean finished);
}
