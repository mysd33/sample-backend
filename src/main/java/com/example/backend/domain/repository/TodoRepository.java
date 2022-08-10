package com.example.backend.domain.repository;

import java.util.Collection;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.backend.domain.model.Todo;

/**
 * TodoのRepositoryインタフェース 
 */
@Mapper
public interface TodoRepository {
	/**
	 * Todoを取得する
	 * @param todoId　TodoのID
	 * @return Todo
	 */
    Optional<Todo> findById(String todoId);

    /**
     * Todoを全件取得する。
     * @return Todoの全件リスト
     */
    Collection<Todo> findAll();
    
    /**
     * Todoを作成する
     * @param todo 作成するTodo
     */
    void create(Todo todo);

    /**
     * Todoを更新する
     * @param todo 更新するTodo
     * @return 更新成功したかどうか
     */
    boolean update(Todo todo);

   /**
    * Todoを削除する
    * @param todo 削除するTodo
    */
    void delete(Todo todo);

    /**
     * 指定した完了ステータスのTodoの件数を取得
     * @param finished 完了ステータス
     * @return 件数
     */
    long countByFinished(boolean finished);
}
