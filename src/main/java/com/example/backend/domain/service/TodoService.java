package com.example.backend.domain.service;

import java.util.Collection;

import com.example.backend.domain.model.Todo;

/**
 * TodoServiceのインタフェース 
 *
 */
public interface TodoService {
    Todo findOne(String todoId);
    
    Collection<Todo> findAll();

    Todo create(Todo todo);

    Todo finish(String todoId);

    void delete(String todoId);
}
