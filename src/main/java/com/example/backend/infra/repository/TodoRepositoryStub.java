package com.example.backend.infra.repository;

import com.example.backend.domain.model.Todo;
import com.example.backend.domain.repository.TodoRepository;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/// TodoRepositoryのスタブ実装
public class TodoRepositoryStub implements TodoRepository {

    private static final Map<String, Todo> TODO_MAP = new ConcurrentHashMap<>();

    @Override
    public Optional<Todo> findOne(String todoId) {
        return Optional.ofNullable(TODO_MAP.get(todoId));
    }

    @Override
    public Optional<Todo> findOneByUserId(String todoId, String userId) {
        var todo = TODO_MAP.get(todoId);
        if (todo != null && todo.getUserId().equals(userId)) {
            return Optional.of(todo);
        }
        return Optional.empty();
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
    public boolean updateFinishedById(String todoId, String userId) {
        var todo = TODO_MAP.get(todoId);
        if (todo != null && todo.getUserId().equals(userId)) {
            todo.setFinished(true);
            TODO_MAP.put(todoId, todo);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Todo todo) {
        TODO_MAP.remove(todo.getTodoId());
        return true;
    }

    @Override
    public boolean deleteById(String todoId, String userId) {
        var todo = TODO_MAP.get(todoId);
        if (todo != null && todo.getUserId().equals(userId)) {
            TODO_MAP.remove(todoId);
            return true;
        }
        return false;
    }

    @Override
    public long countByFinishedStatus(String userId, boolean finished) {
        var count = 0L;
        for (Todo todo : TODO_MAP.values()) {
            if (userId.equals(todo.getUserId()) && finished == todo.isFinished()) {
                count++;
            }
        }
        return count;
    }
}
