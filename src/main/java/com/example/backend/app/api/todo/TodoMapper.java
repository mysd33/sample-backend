package com.example.backend.app.api.todo;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import com.example.backend.domain.model.Todo;

/**
 * TodoとTodoResourceのオブジェクトマッパークラス
 *
 */
@Mapper(componentModel = ComponentModel.SPRING)
public interface TodoMapper {

    /**
     * モデルからリソースに変換
     */
    TodoResource modelToResource(Todo todo);

    /**
     * リソースからモデルに変換
     */
    Todo resourceToModel(TodoResource todoResource);

    /**
     * モデルからリソースに変換
     */    
    default List<TodoResource> modelsToResources(Collection<Todo> todos) {
        return todos.stream().map(this::modelToResource).toList();
    }
}
