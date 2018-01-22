package com.github.amitsk.todos

import com.github.amitsk.todos.handlers.TodosRoutesHandler
import com.github.amitsk.todos.handlers.TodosWebExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router
import org.springframework.web.server.WebExceptionHandler

@Configuration
class TodosConfiguration() {
  @Bean
  fun todosRouter(apiHandler: TodosRoutesHandler) =
      router {
        (accept(MediaType.APPLICATION_JSON) and "/todos").nest {
            GET("/{id}", apiHandler::getTodo)
            PUT("/{id}", apiHandler::updateTodo)
            POST("/", apiHandler::createTodo)
            DELETE("/{id}", apiHandler::deleteTodo)
        }
      }

  @Bean
  @Order(HIGHEST_PRECEDENCE)
  fun todosExceptionHandler(): WebExceptionHandler {
    return  TodosWebExceptionHandler()
  }
}
