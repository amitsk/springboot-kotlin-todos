package com.github.amitsk.todos.handlers

import com.github.amitsk.todos.TodoItem
import com.github.amitsk.todos.TodosApiError
import com.github.amitsk.todos.json
import com.github.amitsk.todos.repository.TodosRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.net.URI

@Component
class TodosHandler(val repository: TodosRepository) {
  companion object {
    val idKey = "id"
  }

  fun getTodo(serverRequest: ServerRequest): Mono<ServerResponse> {
    val todoItemMono = repository.getTodo(serverRequest.pathVariable(idKey).toLong())
    val notFound = ServerResponse.status(HttpStatus.NOT_FOUND).body(TodosApiError.TODO_NOT_FOUND.toMono())
    return todoItemMono.flatMap { itm ->
      ok().json().body(itm.toMono())
    }.switchIfEmpty(notFound)
  }

  fun createTodo(serverRequest: ServerRequest): Mono<ServerResponse> {
    val todoItemMono = serverRequest.bodyToMono<TodoItem>()
    return todoItemMono.map { item ->
      repository.createTodo(item)
    }.flatMap { cItm -> created(URI.create(cItm.id.toString())).build() }
  }

  fun updateTodo(serverRequest: ServerRequest): Mono<ServerResponse> {
    val todoItemMono = serverRequest.bodyToMono<TodoItem>()
    val id = serverRequest.pathVariable(idKey).toLong()

    return todoItemMono.map { item ->
      repository.updateTodo(id, item)
    }.flatMap { cItm -> ok().json().body(cItm.toMono()) }
  }

  fun deleteTodo(serverRequest: ServerRequest): Mono<ServerResponse> {
    repository.deleteTodo(serverRequest.pathVariable(idKey).toLong())
    return noContent().build()
  }

}
