package com.github.amitsk.todos

import com.github.amitsk.todos.handlers.TodosHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class TodosRoutes() {
  @Bean
  fun todosRouter(apiHandler: TodosHandler) =
      router {
        (accept(MediaType.APPLICATION_JSON) and "/todos").nest {
            GET("/{id}", apiHandler::getTodo)
            PUT("/{id}", apiHandler::updateTodo)
            POST("/", apiHandler::createTodo)
            DELETE("/{id}", apiHandler::deleteTodo)
        }
      }
}
