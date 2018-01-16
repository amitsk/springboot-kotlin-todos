package com.github.amitsk.todos.handlers

import com.github.amitsk.todos.TodoItem
import com.github.amitsk.todos.json
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.toMono
import java.net.URI

@Component
class TodosHandler {

  fun getTodo(serverRequest: ServerRequest) = {
    ok().json().body(TodoItem(12L, "task", "Do Something").toMono())
  }

  fun createTodo(serverRequest: ServerRequest) =  {
    val todoItem = serverRequest.bodyToMono<TodoItem>()
    created(URI.create("yo!")).render("welcome")
  }
    fun updateTodo(serverRequest: ServerRequest) = ok().json().render("welcome")
    fun deleteTodo(serverRequest: ServerRequest) = noContent().build()

}
