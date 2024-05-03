package com.github.amitsk.todos.handlers

import com.github.amitsk.todos.ApiError
import com.github.amitsk.todos.ErrorCode
import com.github.amitsk.todos.TodosException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.result.view.ViewResolver
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono
import jakarta.validation.ValidationException

@Component
class TodosWebExceptionHandler : WebExceptionHandler {
  private val logger: Logger = LoggerFactory.getLogger("TodosWebExceptionHandler")

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
        createResponse(
          ApiError(listOf(ErrorCode.TODO_NOT_FOUND), HttpStatus.NOT_FOUND, "Entity not found, details: ${throwable.message}" )
        )
      }
      is ValidationException -> {
        createResponse(ApiError(listOf(ErrorCode.BAD_REQUEST), HttpStatus.BAD_REQUEST, "Bad Request: ${throwable.message}" ))
      }
      else -> {
        logger.error("Generic Error caught ...", throwable)
        createResponse(ApiError(listOf(ErrorCode.GENERIC_ERROR), HttpStatus.INTERNAL_SERVER_ERROR, "${throwable.message}" ))
      }
    }
  }

  fun createResponse(apiError: ApiError): Mono<ServerResponse> =
      ServerResponse.status(apiError.httpStatus).bodyValue(apiError)
}

private class HandlerStrategiesResponseContext(val strategies: HandlerStrategies) : ServerResponse.Context {

  override fun messageWriters(): List<HttpMessageWriter<*>> {
    return this.strategies.messageWriters()
  }

  override fun viewResolvers(): List<ViewResolver> {
    return this.strategies.viewResolvers()
  }
}