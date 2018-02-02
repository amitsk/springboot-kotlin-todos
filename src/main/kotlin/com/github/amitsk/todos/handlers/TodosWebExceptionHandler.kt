package com.github.amitsk.todos.handlers

import com.github.amitsk.todos.TODO_NOT_FOUND_ERROR
import com.github.amitsk.todos.TodosException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.result.view.ViewResolver
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

@Component
class TodosWebExceptionHandler() : WebExceptionHandler {

  override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> =
      handle(ex)
          .flatMap {
            it.writeTo(exchange, HandlerStrategiesResponseContext(HandlerStrategies.withDefaults()))
          }
          .flatMap {
            Mono.empty<Void>()
          }

  fun handle(throwable: Throwable): Mono<ServerResponse> {

    return when (throwable) {
      //TODO Really Really basic. Please add different handlers and process the included information
      //TODO - TRy backstopper
      is TodosException -> {
        createResponse(NOT_FOUND, "NOT_FOUND", "Entity not found, details: ${throwable.message}")
      }
      else -> {
        createResponse(INTERNAL_SERVER_ERROR, "GENERIC_ERROR", "Unhandled exception")
      }
    }
  }

  fun createResponse(httpStatus: HttpStatus, code: String, message: String): Mono<ServerResponse> =
      ServerResponse.status(httpStatus).header("Error", message).syncBody(TODO_NOT_FOUND_ERROR)
}

private class HandlerStrategiesResponseContext(val strategies: HandlerStrategies) : ServerResponse.Context {

  override fun messageWriters(): List<HttpMessageWriter<*>> {
    return this.strategies.messageWriters()
  }

  override fun viewResolvers(): List<ViewResolver> {
    return this.strategies.viewResolvers()
  }
}