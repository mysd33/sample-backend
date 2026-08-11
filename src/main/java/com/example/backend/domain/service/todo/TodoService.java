package com.example.backend.domain.service.todo;

import com.example.backend.domain.model.Todo;
import java.util.Collection;

/// TodoServiceのインタフェース
public interface TodoService {

    /// Todoを一件取得する
    ///
    /// @param todoId ID
    /// @return 取得したTodo
    Todo findOne(String todoId);

    /// Todoを一件取得する（指定したTodoとユーザIDとの整合性チェックあり）
    ///
    /// @param todoId ID
    /// @param userId ユーザID
    /// @return 取得したTodo
    Todo findOne(String todoId, String userId);

    /// 対象のユーザのTodoを全件取得する
    ///
    /// @param userId ユーザID
    /// @return Todoの全件リスト
    Collection<Todo> findAllByUserId(String userId);

    /// Todoを作成する
    ///
    /// @param todo 作成するTodo
    /// @return 作成したTodo
    Todo create(Todo todo);

    /// Todoを作成する Batch用に作成数に制限を置かない。
    ///
    /// @param todo 作成するTodo
    /// @return 作成したTodo
    Todo createForBatch(Todo todo);

    /// Todoを完了する
    ///
    /// @param todoId 完了するTodoのID
    /// @return 完了したTodo
    Todo finish(String todoId);

    /// Todoを完了する（指定したTodoとユーザIDとの整合性チェックあり）
    ///
    /// @param todoId 完了するTodoのID
    /// @param userId 完了するTodoのユーザID
    /// @return 完了したTodo
    Todo finish(String todoId, String userId);

    /// Todoを削除する
    ///
    /// @param todoId 削除するTodoのID
    void delete(String todoId);

    /// Todoを削除する（指定したTodoとユーザIDとの整合性チェックあり）
    ///
    /// @param todoId 削除するTodoのID
    /// @param userId 削除するTodoのユーザID
    void delete(String todoId, String userId);
}
