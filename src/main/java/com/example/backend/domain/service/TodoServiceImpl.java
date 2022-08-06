package com.example.backend.domain.service;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.model.Todo;
import com.example.backend.domain.repository.TodoRepository;
import com.example.fw.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;

/**
 * TodoService実装
 * @author dell
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private static final long MAX_UNFINISHED_COUNT = 5;
    
    private final TodoRepository todoRepository;

    @Override
    @Transactional(readOnly = true) 
    public Collection<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Todo findOne(String todoId) {        
        return todoRepository.findById(todoId).orElseThrow(() -> {
            // リソースがない場合、業務エラー
        	//TODO: メッセージコード
            return new BusinessException("XXX");
        });
    }
    
    @Override
    public Todo create(Todo todo) {
        long unfinishedCount = todoRepository.countByFinished(false);
        if (unfinishedCount >= MAX_UNFINISHED_COUNT) {
            // 未完了のTodoが、5件以上の場合、業務エラー
        	//TODO: メッセージコード
            throw new BusinessException("XXX");
        }

        String todoId = UUID.randomUUID().toString();
        Date createdAt = new Date();
        
        
        todo.setTodoId(todoId);
        todo.setCreatedAt(createdAt);
        todo.setFinished(false);

        todoRepository.create(todo);

        return todo;
    }

    @Override
    public Todo finish(String todoId) {
        Todo todo = findOne(todoId);
        if (todo.isFinished()) {
            //すでに終了している場合、業務エラー
        	//TODO: メッセージコード
            throw new BusinessException("XXX");
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