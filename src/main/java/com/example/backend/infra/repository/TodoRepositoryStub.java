package com.example.backend.infra.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import com.example.backend.domain.model.Todo;
import com.example.backend.domain.repository.TodoRepository;

/// TodoRepositoryのスタブ実装
public class TodoRepositoryStub implements TodoRepository {
    private static final Map<String, Todo> TODO_MAP = new ConcurrentHashMap<>();

    @Override
    public Optional<Todo> findById(String todoId) {
        return Optional.ofNullable(TODO_MAP.get(todoId));
    }

    @Override
    public Collection<Todo> findAllByUserId(String userId) {
        return TODO_MAP.values().stream().filter(todo -> todo.getUserId().equals(userId)).toList();
    }

    @Override
    public void create(Todo todo) {
        TODO_MAP.put(todo.getTodoId(), todo);
    }

    @Override
    public boolean update(Todo todo) {
        TODO_MAP.put(todo.getTodoId(), todo);
        return true;
    }

    @Override
    public boolean delete(Todo todo) {
        TODO_MAP.remove(todo.getTodoId());
        return true;
    }

    @Override
    public long countByFinished(String userId, boolean finished) {
        var count = 0L;
        for (Todo todo : TODO_MAP.values()) {
            if (userId.equals(todo.getUserId()) && finished == todo.isFinished()) {
                count++;
            }
        }
        return count;
    }
}
