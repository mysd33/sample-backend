package com.example.backend.app.api.todo;

import com.example.backend.domain.model.Todo;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

/// TodoとTodoResourceのオブジェクトマッパークラス
@Mapper(componentModel = ComponentModel.SPRING)
public interface TodoMapper {

    /// モデルからリソースに変換
    TodoResource modelToResource(Todo todo);

    /// モデルからリソースに変換
    TodoV2Resource modelTodoV2Resource(Todo todo);

    /// リソースからモデルに変換
    Todo resourceToModel(TodoResource todoResource);

    /// リソースからモデルに変換
    @Mapping(target = "userId", ignore = true)
    Todo resourceToModel(TodoV2Resource todoResource);

    /// モデルからリソースに変換
    default List<TodoResource> modelsToResources(Collection<Todo> todos) {
        return todos.stream().map(this::modelToResource).toList();
    }

    /// モデルからリソースに変換
    default List<TodoV2Resource> modelsToV2Resources(Collection<Todo> todos) {
        return todos.stream().map(this::modelTodoV2Resource).toList();
    }
}
