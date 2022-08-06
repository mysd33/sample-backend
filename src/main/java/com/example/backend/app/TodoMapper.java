package com.example.backend.app;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.backend.domain.model.Todo;

@Mapper
public interface TodoMapper {
	TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);
	TodoResource ModelToResource(Todo todo);
	Todo ResourceToModel(TodoResource todoResource);

}
