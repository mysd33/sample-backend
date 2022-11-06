package com.example.backend.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.example.backend.domain.model.Todo;
import com.example.backend.domain.service.TodoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class TodoRestController {
	private final TodoService todoService;
	
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TodoResource> getTodos() {
        Collection<Todo> todos = todoService.findAll();
        List<TodoResource> todoResources = new ArrayList<>();
        for (Todo todo : todos) {
            todoResources.add(TodoMapper.INSTANCE.modelToResource(todo));
        }
        return todoResources;
    }
    
    @GetMapping("{todoId}") 
    @ResponseStatus(HttpStatus.OK)
    public TodoResource getTodo(@PathVariable("todoId") String todoId) {
        Todo todo = todoService.findOne(todoId); 
        TodoResource todoResource = TodoMapper.INSTANCE.modelToResource(todo);
        return todoResource;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) 
    public TodoResource postTodos(@RequestBody @Validated TodoResource todoResource) { 
        Todo createdTodo = todoService.create(TodoMapper.INSTANCE.resourceToModel(todoResource));
        TodoResource createdTodoResponse = TodoMapper.INSTANCE.modelToResource(createdTodo);
        return createdTodoResponse;
    }
    
    @PostMapping("batch")
    @ResponseStatus(HttpStatus.CREATED) 
    public TodoResource postTodosForBatch(@RequestBody @Validated TodoResource todoResource) { 
        Todo createdTodo = todoService.createForBatch(TodoMapper.INSTANCE.resourceToModel(todoResource));
        TodoResource createdTodoResponse = TodoMapper.INSTANCE.modelToResource(createdTodo);
        return createdTodoResponse;
    }
  

    @PutMapping("{todoId}")
    @ResponseStatus(HttpStatus.OK)
    public TodoResource putTodo(@PathVariable("todoId") String todoId) {
        Todo finishedTodo = todoService.finish(todoId);
        TodoResource finishedTodoResource = TodoMapper.INSTANCE.modelToResource(finishedTodo);
        return finishedTodoResource;
    }
    
    @DeleteMapping("{todoId}") 
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void deleteTodo(@PathVariable("todoId") String todoId) {
        todoService.delete(todoId);
    }
}
