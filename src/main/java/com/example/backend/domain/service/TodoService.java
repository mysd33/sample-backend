package com.example.backend.domain.service;

import java.util.Collection;

import com.example.backend.domain.model.Todo;

/**
 * TodoServiceのインタフェース 
 *
 */
public interface TodoService {
	/**
	 * Todoを一件取得する
	 * @param todoId ID
	 * @return Todo
	 */
    Todo findOne(String todoId);
    
    /**
     * Todoを全件取得する
     * @return Todoの全件リスト
     */
    Collection<Todo> findAll();

    /**
     * Todoを作成する
     * @param todo 作成するTodo
     * @return 作成したTodo
     */
    Todo create(Todo todo);
    
    /**
     * Todoを作成する
     * Batch用に作成数に制限を置かない。
     * @param todo 作成するTodo
     * @return 作成したTodo
     */
    Todo createForBatch(Todo todo);

    /**
     * Todoを完了する
     * @param todoId 完了するTodoのID
     * @return 完了したTodo
     */
    Todo finish(String todoId);

    /**
     * Todoを削除する
     * @param todoId 削除するTodoのID
     */
    void delete(String todoId);
}
