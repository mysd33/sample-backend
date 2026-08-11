package com.example.backend.domain.service.todo;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.example.backend.domain.message.CommonMessageIds;
import com.example.backend.domain.message.MessageIds;
import com.example.backend.domain.model.Todo;
import com.example.backend.domain.repository.TodoRepository;
import com.example.fw.common.exception.BusinessException;
import com.example.fw.common.logging.ApplicationLogger;
import com.example.fw.common.logging.LoggerFactory;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// TodoServiceの実装クラス
@Slf4j
@XRayEnabled
@Service
@Transactional
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private static final ApplicationLogger appLogger = LoggerFactory.getApplicationLogger(log);
    private static final int TRANSACTION_TIMEOUT = 2;
    private static final long MAX_UNFINISHED_COUNT = 5;

    private final TodoRepository todoRepository;

    @Override
    @Transactional(readOnly = true)
    // @Transactional(readOnly = true, timeout=1) // トランザクションタイムアウトを1秒に設定する例
    public Todo findOne(String todoId) {
        return doFindOne(todoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Todo findOne(String todoId, String userId) {
        return todoRepository.findOneByUserId(todoId, userId).orElseThrow(() -> //
            // 対象Todoがない場合、業務エラー
            new BusinessException(MessageIds.W_EX_5001));
    }

    @Override
    // クエリタイムアウトを2秒に設定する例
    @Transactional(readOnly = true, timeout = TRANSACTION_TIMEOUT)
    public Collection<Todo> findAllByUserId(String userId) {
        appLogger.info(CommonMessageIds.I_CMN_0001);
        return todoRepository.findAllByUserId(userId);
    }

    @Override
    public Todo create(Todo todo) {
        var unfinishedCount = todoRepository.countByFinishedStatus(todo.getUserId(), false);
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

    /// Todoを作成する内部処理
    private void doCreate(Todo todo) {
        var todoId = UUID.randomUUID().toString();
        var createdAt = new Date();
        todo.setTodoId(todoId);
        todo.setCreatedAt(createdAt);
        todo.setFinished(false);
        todoRepository.create(todo);
    }

    @Override
    public Todo finish(String todoId) {
        var todo = doFindOne(todoId);
        if (todo.isFinished()) {
            // すでに終了している場合、業務エラー
            throw new BusinessException(MessageIds.W_EX_5003, todoId);
        }
        // 完了状態に更新
        todo.setFinished(true);
        var result = todoRepository.update(todo);
        if (!result) {
            throw new BusinessException(MessageIds.W_EX_5005, todoId);
        }
        return todo;
    }

    @Override
    public Todo finish(String todoId, String userId) {
        var todo = doFindOne(todoId);
        if (todo.isFinished()) {
            // すでに終了している場合、業務エラー
            throw new BusinessException(MessageIds.W_EX_5003, todoId);
        }
        var result = todoRepository.updateFinishedById(todoId, userId);
        if (!result) {
            // 対象のTodoのユーザIDが一致しない場合等、業務エラー
            throw new BusinessException(MessageIds.W_EX_5005, todoId);
        }
        todo.setFinished(true);
        return todo;
    }

    @Override
    public void delete(String todoId) {
        var todo = doFindOne(todoId);
        var result = todoRepository.delete(todo);
        if (!result) {
            throw new BusinessException(MessageIds.W_EX_5005, todoId);
        }
    }

    @Override
    public void delete(String todoId, String userId) {
        var result = todoRepository.deleteById(todoId, userId);
        if (!result) {
            // 対象のTodoのユーザIDが一致しない場合等、業務エラー
            throw new BusinessException(MessageIds.W_EX_5006, todoId);
        }
    }

    private Todo doFindOne(String todoId) {
        return todoRepository.findOne(todoId).orElseThrow(() -> //
            // 対象Todoがない場合、業務エラー
            new BusinessException(MessageIds.W_EX_5001));
    }

}
