package com.example.backend.app.api.todo;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.example.backend.app.api.common.authorization.AuthorizationUtil;
import com.example.backend.domain.message.MessageIds;
import com.example.backend.domain.service.todo.TodoService;
import com.example.fw.common.exception.TransactionTimeoutBusinessException;
import com.example.fw.common.rdb.utils.DatabaseAccessUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/// Todoを扱うREST APIのRestControllerクラス(OIDCによるV2 API)
@Tag(name = "Todo", description = "Todo API")
@XRayEnabled
@RestController
@RequestMapping("/api/v2/todos")
@RequiredArgsConstructor
public class TodoV2RestController {

    private final TodoService todoService;
    private final TodoMapper todoMapper;

    /// Todoリストを取得する
    ///
    /// @return Todoリスト
    @Operation(summary = "Todoリスト取得", description = "Todoリストを取得する。")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TodoV2Resource> getTodos() {
        String userId = AuthorizationUtil.getUserId();
        try {
            // @Transactionalのtimeout属性を指定した処理でトランザクションタイムアウト時に業務例外とする実装例
            var todos = todoService.findAllByUserId(userId);
            return todoMapper.modelsToV2Resources(todos);
        } catch (DataAccessException e) {
            // PostgreSQLのトランザクションタイムアウトエラーなら業務例外に変換しスロー
            if (DatabaseAccessUtils.isQueryTimeout(e)) {
                // BusinessExceptionでラップしてリスロー
                throw new TransactionTimeoutBusinessException(e, MessageIds.W_EX_5004,
                    "Todoリスト取得");
            }
            throw e; // それ以外は、そのまま元の例外をスロー
        }
    }

    /// 指定したTodo IDに対応するTodoを取得する
    ///
    /// @param todoId Todo ID
    /// @return IDに対応するTodo
    @Operation(summary = "Todo取得", description = "指定したTodo IDに対応するTodoを取得する。")
    @GetMapping("{todoId}")
    @ResponseStatus(HttpStatus.OK)
    public TodoV2Resource getTodo(
        @Parameter(description = "Todo ID") @PathVariable @UUID String todoId) {
        var userId = AuthorizationUtil.getUserId();
        var todo = todoService.findOne(todoId, userId);
        return todoMapper.modelTodoV2Resource(todo);
    }

    /// Todoを登録する
    ///
    /// @param todoResource 登録するTodo
    /// @return 登録したTodo
    @Operation(summary = "Todo登録", description = "Todoを登録する。")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoV2Resource postTodos(@Parameter(
        description = "登録するTodo") @RequestBody @Validated TodoV2Resource todoResource) {
        var userId = AuthorizationUtil.getUserId();
        var todo = todoMapper.resourceToModel(todoResource);
        todo.setUserId(userId);
        var createdTodo = todoService.create(todo);
        return todoMapper.modelTodoV2Resource(createdTodo);
    }

    /// バッチ処理向けに登録件数をチェックせずにTodoを登録する
    ///
    /// @param todoResource 登録するTodo
    /// @return 登録したTodo
    @Operation(summary = "バッチ処理用Todo登録", description = "バッチ処理向けに登録件数をチェックせずにTodoを登録する。")
    @PostMapping("batch")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoV2Resource postTodosForBatch(@Parameter(
            description = "登録するTodo") @RequestBody @Validated TodoV2Resource todoResource,
        @AuthenticationPrincipal Jwt jwt) {
        var userId = AuthorizationUtil.getUserId();
        var todo = todoMapper.resourceToModel(todoResource);
        todo.setUserId(userId);
        var createdTodo = todoService.createForBatch(todo);
        return todoMapper.modelTodoV2Resource(createdTodo);
    }

    /// 指定したTodo IDのTodoを完了状態に更新する
    ///
    /// @param todoId Todo ID
    /// @return 更新したTodo
    @Operation(summary = "Todo完了", description = "指定したTodo IDのTodoを完了状態に更新する。")
    @PutMapping("{todoId}")
    @ResponseStatus(HttpStatus.OK)
    public TodoV2Resource putTodo(
        @Parameter(description = "Todo ID") @PathVariable @UUID String todoId) {
        var userId = AuthorizationUtil.getUserId();
        // ユーザIDが一致する場合のみ完了状態に更新する
        var finishedTodo = todoService.finish(todoId, userId);
        return todoMapper.modelTodoV2Resource(finishedTodo);
    }

    /// 指定したTodo IDのTodoを削除する。
    ///
    /// @param todoId Todo ID
    @Operation(summary = "Todo削除", description = "指定したTodo IDのTodoを削除する。")
    @DeleteMapping("{todoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@Parameter(description = "Todo ID") @PathVariable @UUID String todoId) {
        var userId = AuthorizationUtil.getUserId();
        // ユーザIDが一致する場合のみ削除する
        todoService.delete(todoId, userId);
    }


}
