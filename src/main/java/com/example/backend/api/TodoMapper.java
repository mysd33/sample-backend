package com.example.backend.api;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.backend.domain.model.Todo;

/**
 * TodoとTodoResourceのオブジェクトマッパークラス 
 *
 */
@Mapper
public interface TodoMapper {
	/**
	 * マッパーインスタンス
	 */
	TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);
	/**
	 * モデルからリソースに変換
	 */
	TodoResource modelToResource(Todo todo);
	/**
	 * リソースからモデルに変換
	 */
	Todo resourceToModel(TodoResource todoResource);

}
