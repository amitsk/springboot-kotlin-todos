package com.github.amitsk.todos.handlers

import com.github.amitsk.todos.TodoItem
import com.github.amitsk.todos.json
import com.github.amitsk.todos.repository.TodosRepository
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

  fun getTodo(serverRequest: ServerRequest): Mono<ServerResponse> =
      ok().json().body(TodoItem(12L, "task", "Do Something").toMono())


  fun createTodo(serverRequest: ServerRequest): Mono<ServerResponse> {
    val todoItem = serverRequest.bodyToMono<TodoItem>()
    return created(URI.create("yo!")).render("welcome")
  }

  fun updateTodo(serverRequest: ServerRequest): Mono<ServerResponse> = ok().json().render("welcome")
  fun deleteTodo(serverRequest: ServerRequest): Mono<ServerResponse> = noContent().build()

}
