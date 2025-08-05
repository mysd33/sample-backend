package com.example.backend.domain.service.todo;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.example.backend.domain.message.MessageIds;
import com.example.backend.domain.model.Todo;
import com.example.backend.domain.repository.TodoRepository;
import com.example.fw.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;

/**
 * TodoServiceの実装クラス
 */
@XRayEnabled
@Service
@Transactional
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private static final int TRANSACTION_TIMEOUT = 2;
    private static final long MAX_UNFINISHED_COUNT = 5;

    private final TodoRepository todoRepository;

    @Override
    // クエリタイムアウトを2秒に設定する例
    @Transactional(readOnly = true, timeout = TRANSACTION_TIMEOUT)
    public Collection<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    // @Transactional(readOnly = true, timeout=1) // トランザクションタイムアウトを1秒に設定する例
    public Todo findOne(String todoId) {
        return todoRepository.findById(todoId).orElseThrow(() -> //
            // 対象Todoがない場合、業務エラー
            new BusinessException(MessageIds.W_EX_5001)
        );
    }

    @Override
    public Todo create(Todo todo) {
        long unfinishedCount = todoRepository.countByFinished(false);
        if (unfinishedCount >= MAX_UNFINISHED_COUNT) {
            // 未完了のTodoが、5件以上の場合、業務エラー
            throw new BusinessException(MessageIds.W_EX_5002, String.valueOf(MAX_UNFINISHED_COUNT));
        }
        doCreate(todo);
        return todo;
    }

    @Override
    public Todo createForBatch(Todo todo) {
        doCreate(todo);

        return todo;
    }

    private void doCreate(Todo todo) {
        String todoId = UUID.randomUUID().toString();
        Date createdAt = new Date();
        todo.setTodoId(todoId);
        todo.setCreatedAt(createdAt);
        todo.setFinished(false);
        todoRepository.create(todo);
    }

    @Override
    public Todo finish(String todoId) {
        Todo todo = findOne(todoId);
        if (todo.isFinished()) {
            // すでに終了している場合、業務エラー
            throw new BusinessException(MessageIds.W_EX_5003, todoId);
        }
        todo.setFinished(true);
        todoRepository.update(todo);
        return todo;
    }

    @Override
    public void delete(String todoId) {
        Todo todo = findOne(todoId);
        todoRepository.delete(todo);
    }

}